package com.santander.bp.app.mapper;

import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.SubProductCosmosDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CosmosDbMapper {

  public List<OffersPricingResponse> mapOffers(List<OfferCosmosDTO> offers) {
    return offers.stream().map(this::mapToOfferResponseDTO).collect(Collectors.toList());
  }

  public OffersPricingResponse mapToOfferResponseDTO(OfferCosmosDTO offer) {
    SubProductCosmosDTO subProduct = getBestSubProduct(offer);

    OffersPricingResponse response =
        OffersPricingResponse.builder()
            .id(offer.getId())
            .product(offer.getProduct())
            .subProduct(subProduct != null ? subProduct.getNmSubp() : offer.getProductDescription())
            .family(offer.getFamily())
            .subProductCode(subProduct != null ? subProduct.getCdSubp() : null)
            .minApplicationValue(
                convertToDouble(subProduct != null ? subProduct.getVlMiniApli() : null))
            .minRedemptionValue(
                convertToDouble(subProduct != null ? subProduct.getVlMiniResg() : null))
            .minBalance(convertToDouble(subProduct != null ? subProduct.getVlMiniSald() : null))
            .build();

    log.info(
        "Mapeando oferta do Cosmos: ID={}, Produto={}, SubProduto={}, Código SubProduto={}, Aplicação Mínima={}, Resgate Mínimo={}, Saldo Mínimo={}",
        response.getId(),
        response.getProduct(),
        response.getSubProduct(),
        response.getSubProductCode(),
        response.getMinApplicationValue(),
        response.getMinRedemptionValue(),
        response.getMinBalance());

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
