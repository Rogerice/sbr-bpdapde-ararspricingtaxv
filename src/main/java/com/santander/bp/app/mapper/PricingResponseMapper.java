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
      log.warn("Lista de condições de pricing está vazia. Retornando apenas os dados do Cosmos.");
      return cosmosOffers;
    }

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
                  rateTerms =
                      matchedPricing.getPrice().getTiers().stream()
                          .filter(
                              tier -> tier.getTerm() != null && tier.getAprPeriodInterest() != null)
                          .map(
                              tier ->
                                  RateTerm.builder()
                                      .term(tier.getTerm().getDays())
                                      .rate(
                                          Double.valueOf(
                                              tier.getAprPeriodInterest())) // ✅ Alterado de
                                      // parseDouble para
                                      // valueOf (evita boxing
                                      // implícito)
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
                    false); // ✅ Boolean boxing mantido simples (Sonar warning aceito ou ajustar
                // tipo na model)
              },
              () ->
                  log.info(
                      "Oferta sem match no pricing. ID Subproduto: {}", offer.getSubProductCode()));

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
                            Double.valueOf(
                                tier.getAprPeriodInterest())) // ✅ Alterado de parseDouble para
                        // valueOf (evita boxing implícito)
                        .build();
                rateTerms.add(rateTerm);
              }
            }
          }

          offer.setRateTerm(rateTerms);
          offer.setBenchmarkIndex(
              price.getBenchmarkIndex() != null ? price.getBenchmarkIndex().getCode() : null);
          offer.setPromotionalCode(
              price.getPromotionalCode() != null
                  ? price.getPromotionalCode().getPromotionalCodeId()
                  : null);
          offer.setProgressiveRemunerationIndicator(
              false); // ✅ Boolean boxing mantido simples (Sonar warning aceito ou ajustar tipo na
          // model)

          break; // encontrou o match, vai pro próximo offer
        }
      }
    }

    return cosmosOffers;
  }
}
