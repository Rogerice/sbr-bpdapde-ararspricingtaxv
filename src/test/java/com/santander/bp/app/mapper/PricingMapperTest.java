package com.santander.bp.app.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.PricingRequest;
import com.santander.bp.model.PricingRequestProposalsInner;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PricingMapperTest {

  private final PricingMapper pricingMapper = new PricingMapper();

  @Test
  void shouldMapPricingRequestToOffersPricingResponse() {
    // given
    PricingRequest request = new PricingRequest();
    request.setVoucher("VOUCHER-123");
    request.setAppliedAmount(BigDecimal.valueOf(10000));

    PricingRequestProposalsInner proposal = new PricingRequestProposalsInner();
    proposal.setProduct("Produto1");
    proposal.setSubProduct("SubProduto1");
    proposal.setIndexType("CDI");
    proposal.setTotalTerm(BigDecimal.valueOf(200));
    proposal.setProgressiveTerms(
        Arrays.asList(
            BigDecimal.valueOf(30),
            BigDecimal.valueOf(90),
            BigDecimal.valueOf(180),
            BigDecimal.valueOf(210)));

    request.setProposals(List.of(proposal));

    List<OffersPricingResponse> responses = pricingMapper.mapToOffersPricingResponse(request);

    assertThat(responses).hasSize(1);
    OffersPricingResponse response = responses.get(0);

    assertThat(response.getProduct()).isEqualTo("Produto1");
    assertThat(response.getSubProduct()).isEqualTo("SubProduto1");
    assertThat(response.getFamily()).isEqualTo("CDI");
    assertThat(response.getVoucher()).isEqualTo("VOUCHER-123");
    assertThat(response.getFunding()).isNotNull();
    assertThat(response.getRateTerm()).hasSize(4);
    assertThat(response.getRateTerm().get(0).getTerm()).isEqualTo(30);
    assertThat(response.getRate()).isNotNull();
  }
}
