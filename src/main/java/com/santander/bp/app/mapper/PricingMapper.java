package com.santander.bp.app.mapper;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.OffersPricingResponseRateTermInner;
import com.santander.bp.model.PricingRequest;
import com.santander.bp.model.PricingRequestProposalsInner;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PricingMapper {

  private static final SecureRandom RANDOM = new SecureRandom();

  public List<OffersPricingResponse> mapToOffersPricingResponse(PricingRequest request) {
    return request.getProposals().stream()
        .map(proposal -> buildResponse(proposal, request))
        .collect(Collectors.toList());
  }

  private OffersPricingResponse buildResponse(
      PricingRequestProposalsInner proposal, PricingRequest request) {

    BigDecimal baseRate = generateRate(proposal.getIndexType(), proposal.getTotalTerm());

    List<OffersPricingResponseRateTermInner> rateTerms =
        proposal.getProgressiveTerms().stream()
            .map(
                term ->
                    new OffersPricingResponseRateTermInner(
                        term.intValue(),
                        formatDecimal(baseRate.add(adjustProgressiveRate(term.intValue())))
                            .doubleValue()))
            .collect(Collectors.toList());

    return OffersPricingResponse.builder()
        .idAdapter("ADP-" + RANDOM.nextInt(100000))
        .idPricingPolicy("PREC-" + RANDOM.nextInt(1000))
        .product(proposal.getProduct())
        .subProduct(proposal.getSubProduct())
        .family(proposal.getIndexType())
        .rate(rateTerms.get(0).getRate())
        .rateTerm(rateTerms)
        .term(proposal.getTotalTerm() != null ? proposal.getTotalTerm().intValue() : null)
        .voucher(request.getVoucher())
        .funding(calculateFunding(request.getAppliedAmount()))
        .reason("Simulação de pricing mockada")
        .build();
  }

  private BigDecimal generateRate(String indexType, BigDecimal totalTerm) {
    BigDecimal baseRateCDI = BigDecimal.valueOf(12.0);
    BigDecimal baseRateIPCA = BigDecimal.valueOf(6.0);
    BigDecimal baseRatePRE = BigDecimal.valueOf(10.0);

    BigDecimal rate;
    if ("CDI".equalsIgnoreCase(indexType)) {
      rate = baseRateCDI.multiply(BigDecimal.valueOf(0.98 + RANDOM.nextDouble() * 0.12));
    } else if ("IPCA".equalsIgnoreCase(indexType)) {
      rate = baseRateIPCA.add(BigDecimal.valueOf(RANDOM.nextDouble() * 3));
    } else if ("PRE".equalsIgnoreCase(indexType)) {
      rate = baseRatePRE.add(BigDecimal.valueOf(RANDOM.nextDouble() * 3));
    } else {
      rate = BigDecimal.valueOf(10.0);
    }

    if (totalTerm != null && totalTerm.compareTo(BigDecimal.valueOf(180)) > 0) {
      rate = rate.add(BigDecimal.valueOf(0.5));
    }

    return rate;
  }

  private BigDecimal adjustProgressiveRate(Integer term) {
    if (term <= 90) {
      return BigDecimal.valueOf(-0.2);
    } else if (term <= 180) {
      return BigDecimal.ZERO;
    } else {
      return BigDecimal.valueOf(0.3);
    }
  }

  private BigDecimal calculateFunding(BigDecimal appliedAmount) {
    BigDecimal multiplier = BigDecimal.valueOf(0.98 + RANDOM.nextDouble() * 0.04);
    return formatDecimal(appliedAmount.multiply(multiplier));
  }

  private BigDecimal formatDecimal(BigDecimal value) {
    return value.setScale(3, RoundingMode.HALF_UP);
  }
}
