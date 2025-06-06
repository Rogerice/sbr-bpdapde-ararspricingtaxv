package com.santander.bp.app.mapper;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.RateTerm;
import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import com.santander.bp.model.external.Tier;
import java.util.ArrayList;
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

  public List<OffersPricingResponse> mergePricingWithCosmos(
      List<OffersPricingResponse> cosmosOffers,
      List<InvestmentPricingConditionResponse> pricingData) {

    if (pricingData == null || pricingData.isEmpty()) {
      log.warn(
          "⚠️ Lista de condições de pricing está vazia. Retornando apenas os dados do Cosmos.");
      return cosmosOffers;
    }

    // Melhoria para a performance: pré-dimensionar a lista 'enriched'
    // A lista 'enriched' terá no máximo o mesmo tamanho de 'cosmosOffers'
    List<OffersPricingResponse> enriched = new ArrayList<>(cosmosOffers.size());

    for (OffersPricingResponse offer : cosmosOffers) {
      pricingData.stream()
          .filter(
              p ->
                  p.getProduct() != null
                      && p.getProduct().getSubproduct() != null
                      && Objects.equals(
                          p.getProduct().getSubproduct().getCode(), offer.getSubProductCode()))
          .findFirst()
          .ifPresentOrElse(
              matchedPricing -> {
                List<RateTerm> rateTerms = new ArrayList<>();
                if (matchedPricing.getPrice() != null
                    && matchedPricing.getPrice().getTiers() != null) {
                  // Pré-dimensionar rateTerms também
                  rateTerms =
                      matchedPricing.getPrice().getTiers().stream()
                          .filter(
                              tier -> tier.getTerm() != null && tier.getAprPeriodInterest() != null)
                          .map(
                              tier ->
                                  RateTerm.builder()
                                      .term(tier.getTerm().getDays())
                                      // .rate(Double.valueOf(tier.getAprPeriodInterest())) // Sonar
                                      // avisou aqui
                                      .rate(
                                          Double.parseDouble(
                                              tier.getAprPeriodInterest())) // parseDouble é o mais
                                      // comum, e autoboxing
                                      // ocorrerá para o
                                      // Double do RateTerm
                                      .build())
                          .collect(Collectors.toList());
                }

                offer.setRateTerm(rateTerms);
                offer.setBenchmarkIndex(
                    matchedPricing.getBenchmarkIndex() != null
                        ? matchedPricing.getBenchmarkIndex().getCode()
                        : null);
                offer.setPromotionalCode(
                    matchedPricing.getPromotionalCode() != null
                        ? matchedPricing.getPromotionalCode().getPromotionalCodeId()
                        : null);
                offer.setProgressiveRemunerationIndicator(
                    false); // O aviso de boxing é geralmente ignorado aqui
              },
              () ->
                  log.info(
                      "➡️ Oferta sem match no pricing. ID Subproduto: {}",
                      offer.getSubProductCode()));

      enriched.add(offer);
    }

    return enriched;
  }

  public List<OffersPricingResponse> mergeCosmosWithPricing(
      List<OffersPricingResponse> cosmosOffers,
      List<InvestmentPricingConditionResponse> pricingData) {

    for (OffersPricingResponse offer : cosmosOffers) {
      for (InvestmentPricingConditionResponse price : pricingData) {

        if (price.getProduct() != null
            && price.getProduct().getSubproduct() != null
            && offer.getProduct().equals(price.getProduct().getBusinessCategoryCode())
            && offer.getSubProductCode().equals(price.getProduct().getSubproduct().getCode())) {

          // Mapeando tiers para RateTerm
          // Melhoria para a performance: pré-dimensionar rateTerms
          List<RateTerm> rateTerms =
              new ArrayList<>(
                  price.getPrice() != null && price.getPrice().getTiers() != null
                      ? price.getPrice().getTiers().size()
                      : 0);
          if (price.getPrice() != null && price.getPrice().getTiers() != null) {
            for (Tier tier : price.getPrice().getTiers()) {
              if (tier.getTerm() != null && tier.getAprPeriodInterest() != null) {
                RateTerm rateTerm =
                    RateTerm.builder()
                        .term(tier.getTerm().getDays())
                        .rate(
                            Double.parseDouble(
                                tier.getAprPeriodInterest())) // Sonar avisou aqui, mas está ok para
                        // Double.
                        .build();
                rateTerms.add(rateTerm);
              }
            }
          }

          offer.setRateTerm(rateTerms);

          // Benchmark
          offer.setBenchmarkIndex(
              price.getBenchmarkIndex() != null ? price.getBenchmarkIndex().getCode() : null);

          // Promotional code
          offer.setPromotionalCode(
              price.getPromotionalCode() != null
                  ? price.getPromotionalCode().getPromotionalCodeId()
                  : null);

          // Progressive indicator - usar regra se tiver
          offer.setProgressiveRemunerationIndicator(
              false); // O aviso de boxing é geralmente ignorado aqui

          break; // encontrou o match, vai pro próximo offer
        }
      }
    }

    return cosmosOffers;
  }
}
