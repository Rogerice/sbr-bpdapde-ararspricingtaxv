package com.santander.bp.service;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.OffersPricingResponseRateTermInner;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PricingEngineService {

  public List<OffersPricingResponse> enrichOffersWithMockRates(List<OffersPricingResponse> offers) {
    return offers.stream().map(this::generateMockRate).collect(Collectors.toList());
  }

  private OffersPricingResponse generateMockRate(OffersPricingResponse offer) {
    List<OffersPricingResponseRateTermInner> rateTerms =
        List.of(
            new OffersPricingResponseRateTermInner(180, Math.random() * 5 + 10),
            new OffersPricingResponseRateTermInner(365, Math.random() * 5 + 10));

    return OffersPricingResponse.builder()
        .id(offer.getId())
        .product(offer.getProduct())
        .subProductCode(offer.getSubProductCode())
        .subProduct(offer.getSubProduct())
        .family(offer.getFamily())
        .feeDescription(offer.getFeeDescription())
        .closingFee(offer.getClosingFee())
        .receivingFee(offer.getReceivingFee())
        .messages(offer.getMessages())
        .term(getLowestTerm(rateTerms))
        .rate(rateTerms.get(0).getRate())
        .rateTerm(rateTerms)
        .campaign("Mocked Pricing - BP")
        .funding(Math.random() * 1000000)
        .reason("Mocked Reason")
        .build();
  }

  protected Integer getLowestTerm(List<OffersPricingResponseRateTermInner> rateTerms) {
    return rateTerms.stream()
        .map(OffersPricingResponseRateTermInner::getTerm)
        .min(Integer::compareTo)
        .orElse(null);
  }
}
