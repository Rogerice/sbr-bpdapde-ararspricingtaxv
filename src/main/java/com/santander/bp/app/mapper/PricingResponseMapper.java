package com.santander.bp.app.mapper;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.RateTerm;
import com.santander.bp.model.external.InvestmentPricingConditionResponse;
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
          .filter(p -> isSubproductMatching(p, offer.getSubProductCode()))
          .findFirst()
          .ifPresentOrElse(
              matchedPricing -> enrichOffer(offer, matchedPricing),
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
        if (isFullMatch(offer, price)) {
          enrichOffer(offer, price);
          break;
        }
      }
    }

    return cosmosOffers;
  }

  private boolean isSubproductMatching(
      InvestmentPricingConditionResponse p, String subProductCode) {
    return p.getProduct() != null
        && p.getProduct().getSubproduct() != null
        && Objects.equals(p.getProduct().getSubproduct().getCode(), subProductCode);
  }

  private boolean isFullMatch(
      OffersPricingResponse offer, InvestmentPricingConditionResponse price) {
    return price.getProduct() != null
        && price.getProduct().getSubproduct() != null
        && offer.getProduct().equals(price.getProduct().getBusinessCategoryCode())
        && offer.getSubProductCode().equals(price.getProduct().getSubproduct().getCode());
  }

  private void enrichOffer(OffersPricingResponse offer, InvestmentPricingConditionResponse price) {
    offer.setRateTerm(buildRateTerms(price));
    offer.setBenchmarkIndex(
        price.getBenchmarkIndex() != null ? price.getBenchmarkIndex().getCode() : null);
    offer.setPromotionalCode(
        price.getPromotionalCode() != null
            ? price.getPromotionalCode().getPromotionalCodeId()
            : null);
    offer.setProgressiveRemunerationIndicator(Boolean.FALSE);
  }

  private List<RateTerm> buildRateTerms(InvestmentPricingConditionResponse price) {
    if (price.getPrice() == null || price.getPrice().getTiers() == null) {
      return new ArrayList<>();
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
