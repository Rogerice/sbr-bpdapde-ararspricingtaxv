package com.santander.bp.service;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.OffersPricingResponseRateTermInner;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
  private static final double BASE_RATE_CDI = 12.0; // Estimativa do CDI
  private static final double BASE_RATE_IPCA = 6.0; // Inflação projetada
  private static final double BASE_RATE_PRE = 10.0; // Juros pré-fixados médios

  public List<OffersPricingResponse> enrichOffersWithMockRates(
      Collection<OffersPricingResponse> offers) {
    return offers.stream().map(this::generateMockRate).collect(Collectors.toList());
  }

  private OffersPricingResponse generateMockRate(OffersPricingResponse offer) {
    List<OffersPricingResponseRateTermInner> rateTerms =
        List.of(
            new OffersPricingResponseRateTermInner(
                TERM_180_DAYS, formatDecimal(generateRate(offer.getFamily()))),
            new OffersPricingResponseRateTermInner(
                TERM_365_DAYS, formatDecimal(generateRate(offer.getFamily()))));

    return OffersPricingResponse.builder()
        .idAdapter(offer.getIdAdapter())
        .product(offer.getProduct())
        .subProductCode(offer.getSubProductCode())
        .subProduct(offer.getSubProduct())
        .family(offer.getFamily())
        .feeDescription(offer.getFeeDescription())
        .closingFee(offer.getClosingFee())
        .receivingFee(offer.getReceivingFee())
        .messages(offer.getMessages())
        .term(getLowestTerm(rateTerms))
        .rate(rateTerms.get(0).getRate()) // Pegando a taxa do menor prazo
        .rateTerm(rateTerms)
        .voucher("Mocked Pricing - BP")
        .funding(BigDecimal.valueOf(generateFunding())) // **Correção: Conversão para BigDecimal**
        .reason("Mocked Reason")
        .build();
  }

  private double generateRate(String indexType) {
    if ("CDI".equalsIgnoreCase(indexType)) {
      return BASE_RATE_CDI * (0.98 + RANDOM.nextDouble() * 0.12); // 98% a 110% do CDI
    } else if ("PRE".equalsIgnoreCase(indexType)) {
      return BASE_RATE_PRE + RANDOM.nextDouble() * 3; // 10% a 13%
    } else if ("LCA".equalsIgnoreCase(indexType)) {
      return BASE_RATE_CDI * (1.00 + RANDOM.nextDouble() * 0.05); // 100% a 105% do CDI
    } else if ("LCI".equalsIgnoreCase(indexType)) {
      return BASE_RATE_CDI * (0.95 + RANDOM.nextDouble() * 0.10); // 95% a 105% do CDI
    } else if ("LIG".equalsIgnoreCase(indexType)) {
      return BASE_RATE_CDI * (1.02 + RANDOM.nextDouble() * 0.08); // 102% a 110% do CDI
    } else if ("LF".equalsIgnoreCase(indexType)) {
      return BASE_RATE_PRE + RANDOM.nextDouble() * 2; // 10% a 12%
    } else if ("COMPROMISSADAS".equalsIgnoreCase(indexType)) {
      return BASE_RATE_CDI * (0.90 + RANDOM.nextDouble() * 0.15); // 90% a 105% do CDI
    } else {
      return 10.0; // Default para indexadores desconhecidos
    }
  }

  private double generateFunding() {
    double funding = 100_000 + (RANDOM.nextDouble() * 900_000); // Entre 100.000 e 1.000.000
    return formatDecimal(funding);
  }

  private double formatDecimal(double value) {
    return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP).doubleValue();
  }

  protected Integer getLowestTerm(Collection<OffersPricingResponseRateTermInner> rateTerms) {
    return rateTerms.stream()
        .map(OffersPricingResponseRateTermInner::getTerm)
        .min(Integer::compareTo)
        .orElse(null);
  }
}
