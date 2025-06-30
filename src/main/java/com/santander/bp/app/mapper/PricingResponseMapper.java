package com.santander.bp.app.mapper;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.RateTerm;
import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Generated
public class PricingResponseMapper {

  /**
   * Mescla os dados das ofertas do CosmosDB com os dados de precificação. ESTA É A VERSÃO FINAL que
   * implementa a lógica de enriquecimento.
   *
   * @param cosmosOffers A lista de ofertas base vinda do Cosmos.
   * @param pricingData A resposta do serviço de pricing.
   * @return A lista de ofertas do Cosmos, enriquecida com as taxas ou com a mensagem de negócio do
   *     pricing.
   */
  public List<OffersPricingResponse> mergeCosmosWithPricing(
      List<OffersPricingResponse> cosmosOffers,
      List<InvestmentPricingConditionResponse> pricingData) {

    // Cenário 1: O serviço de pricing retornou uma única resposta com uma mensagem de "reason".
    // Isso indica um "erro" de negócio, como "subproduto não encontrado".
    if (isBusinessReasonResponse(pricingData)) {
      String reasonMessage = pricingData.get(0).getReason();
      log.warn(
          "Recebida mensagem de negócio do Pricing: '{}'. Adicionando a todas as ofertas.",
          reasonMessage);

      // Adiciona a mensagem de erro a todas as ofertas que seriam retornadas.
      for (OffersPricingResponse offer : cosmosOffers) {
        if (offer.getMessages() == null) {
          offer.setMessages(new ArrayList<>());
        }
        offer.getMessages().add(reasonMessage);
      }
      return cosmosOffers;
    }

    // Cenário 2: Resposta de sucesso. Mescla os dados de taxa para cada oferta correspondente.
    for (OffersPricingResponse offer : cosmosOffers) {
      pricingData.stream()
          // Encontra a condição de pricing correspondente pelo código do subproduto.
          .filter(price -> isSubproductMatching(price, offer.getSubProductCode()))
          .findFirst()
          .ifPresent(matchedPricing -> enrichOffer(offer, matchedPricing));
    }

    return cosmosOffers;
  }

  /**
   * Verifica se a resposta do pricing é uma simples mensagem de negócio. A condição é: a lista tem
   * exatamente um item, e esse item tem o campo 'reason' preenchido, mas os campos 'product' e
   * 'price' estão nulos.
   */
  private boolean isBusinessReasonResponse(List<InvestmentPricingConditionResponse> pricingData) {
    return pricingData != null
        && pricingData.size() == 1
        && pricingData.get(0).getReason() != null
        && pricingData.get(0).getProduct() == null
        && pricingData.get(0).getPrice() == null;
  }

  /** Verifica se o código do subproduto em uma condição de pricing corresponde ao de uma oferta. */
  private boolean isSubproductMatching(
      InvestmentPricingConditionResponse p, String subProductCode) {
    return p.getProduct() != null
        && p.getProduct().getSubproduct() != null
        && Objects.equals(p.getProduct().getSubproduct().getCode(), subProductCode);
  }

  /** Enriquece uma única oferta com os dados de uma condição de pricing. */
  private void enrichOffer(OffersPricingResponse offer, InvestmentPricingConditionResponse price) {
    offer.setRateTerm(buildRateTerms(price));
    offer.setBenchmarkIndex(
        price.getBenchmarkIndex() != null ? price.getBenchmarkIndex().getCode() : null);
    offer.setPromotionalCode(
        price.getPromotionalCode() != null
            ? price.getPromotionalCode().getPromotionalCodeId()
            : null);
    // Adicione aqui outros campos que venham na resposta de sucesso do pricing.
  }

  /** Constrói a lista de termos e taxas a partir de uma resposta de pricing. */
  private List<RateTerm> buildRateTerms(InvestmentPricingConditionResponse price) {
    if (price.getPrice() == null || price.getPrice().getTiers() == null) {
      return Collections.emptyList();
    }

    return price.getPrice().getTiers().stream()
        .filter(t -> t.getTerm() != null && t.getAprPeriodInterest() != null)
        .map(
            t ->
                RateTerm.builder()
                    .term(t.getTerm().getDays())
                    .rate(Double.valueOf(t.getAprPeriodInterest()))
                    .build())
        .collect(Collectors.toList());
  }
}
