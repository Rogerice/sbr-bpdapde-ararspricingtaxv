package com.santander.bp.app.mapper;

import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.SubProductCosmosDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Generated
public class CosmosDbMapper {

  public List<OffersPricingResponse> mapOffers(List<OfferCosmosDTO> offers) {
    return offers.stream()
        .flatMap(
            offer ->
                offer.getSubProducts().stream()
                    .map(subProduct -> mapToOfferResponseDTO(offer, subProduct)))
        .collect(Collectors.toList());
  }

  public OffersPricingResponse mapToOfferResponseDTO(
      OfferCosmosDTO offer, SubProductCosmosDTO subProduct) {

    OffersPricingResponse response =
        OffersPricingResponse.builder()
            //  --- Mapeamentos básicos ---
            .product(offer.getProduct())
            .subProduct(subProduct.getNmSubp())
            .family(offer.getFamily())
            .subProductCode(subProduct.getCdSubp())

            // --- Mapeamentos de valores ---
            .minApplicationValue(convertToDouble(subProduct.getVlMiniApli()))
            .minRedemptionValue(convertToDouble(subProduct.getVlMiniResg()))
            .minBalance(convertToDouble(subProduct.getVlMiniSald()))

            // --- Mapeamentos de campos adicionais do Cosmos ---
            .benchmarkIndex(subProduct.getDsIndx()) // Mapeado de 'DS_INDX'
            .gracePeriodIndicator(
                "S".equalsIgnoreCase(subProduct.getInCare())) // Mapeado de 'IN_CARE'
            .progressiveRemunerationIndicator(
                "S".equalsIgnoreCase(subProduct.getInRemuPgre())) // Mapeado de 'IN_REMU_PGRE'
            .build();

    log.info(
        "Mapeando oferta do Cosmos: OfferId={}, Product={}, SubProductCode={}, Benchmark={}, GracePeriod={}, ProgRemun={}",
        offer.getId(),
        response.getProduct(),
        response.getSubProductCode(),
        response.getBenchmarkIndex(),
        response.isGracePeriodIndicator(),
        response.isProgressiveRemunerationIndicator());

    return response;
  }

  protected SubProductCosmosDTO getBestSubProduct(OfferCosmosDTO offer) {
    if (offer.getSubProducts() == null || offer.getSubProducts().isEmpty()) {
      log.warn("Nenhum subproduto encontrado para a oferta ID: {}", offer.getId());
      return null;
    }

    log.info(
        "Subprodutos disponíveis para a oferta ID {}: {}", offer.getId(), offer.getSubProducts());
    return offer.getSubProducts().stream()
        .min(
            (s1, s2) ->
                Integer.compare(
                    s1.getVlMiniApli() != null ? s1.getVlMiniApli() : Integer.MAX_VALUE,
                    s2.getVlMiniApli() != null ? s2.getVlMiniApli() : Integer.MAX_VALUE))
        .orElse(null);
  }

  protected Double convertToDouble(Integer value) {
    Double result = (value != null) ? value.doubleValue() : null;
    log.debug("Convertendo Integer para Double: Entrada={}, Saída={}", value, result);
    return result;
  }
}
