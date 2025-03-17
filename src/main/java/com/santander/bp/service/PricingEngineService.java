package com.santander.bp.service;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.OffersPricingResponseRateTermInner;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PricingEngineService {

  private static final SecureRandom RANDOM = new SecureRandom();
  private static final int TERM_180_DAYS = 180;
  private static final int TERM_365_DAYS = 365;
  private static final double MIN_RATE = 10.0;
  private static final double MAX_RATE = 5.0;
  private static final double MAX_FUNDING = 1_000_000.0;

  public List<OffersPricingResponse> enrichOffersWithMockRates(
      Collection<OffersPricingResponse> offers) {
    return offers.stream().map(this::generateMockRate).collect(Collectors.toList());
  }

  private OffersPricingResponse generateMockRate(OffersPricingResponse offer) {
    List<OffersPricingResponseRateTermInner> rateTerms =
        List.of(
            new OffersPricingResponseRateTermInner(
                TERM_180_DAYS, RANDOM.nextDouble() * MAX_RATE + MIN_RATE),
            new OffersPricingResponseRateTermInner(
                TERM_365_DAYS, RANDOM.nextDouble() * MAX_RATE + MIN_RATE));

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
        .funding(RANDOM.nextDouble() * MAX_FUNDING)
        .reason("Mocked Reason")
        .build();
  }

  protected Integer getLowestTerm(Collection<OffersPricingResponseRateTermInner> rateTerms) {
    return rateTerms.stream()
        .map(OffersPricingResponseRateTermInner::getTerm)
        .min(Integer::compareTo)
        .orElse(null);
  }
}
