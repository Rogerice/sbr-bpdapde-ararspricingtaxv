package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.santander.bp.app.mapper.PricingMapper;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.PricingRequest;
import com.santander.bp.model.PricingRequestProposalsInner;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PricingProcessingServiceTest {

  private PricingMapper pricingMapper;
  private PricingProcessingService pricingProcessingService;

  @BeforeEach
  void setUp() {
    pricingMapper = mock(PricingMapper.class);
    pricingProcessingService = new PricingProcessingService(pricingMapper);
  }

  @Test
  void shouldReturnPricingResponseSuccessfully() throws Exception {
    PricingRequest request =
        PricingRequest.builder()
            .voucher("MOCK123")
            .appliedAmount(BigDecimal.valueOf(1000))
            .proposals(
                List.of(
                    PricingRequestProposalsInner.builder()
                        .indexType("CDI")
                        .product("CDB")
                        .subProduct("0013")
                        .totalTerm(BigDecimal.valueOf(180))
                        .progressiveTerms(
                            Arrays.asList(BigDecimal.valueOf(30), BigDecimal.valueOf(90)))
                        .build()))
            .build();

    OffersPricingResponse mockResponse =
        OffersPricingResponse.builder()
            .product("CDB")
            .subProduct("0013")
            .rate(12.0)
            .voucher("MOCK123")
            .build();

    when(pricingMapper.mapToOffersPricingResponse(any())).thenReturn(List.of(mockResponse));

    CompletableFuture<List<OffersPricingResponse>> resultFuture =
        pricingProcessingService.processPricing(request);
    List<OffersPricingResponse> result = resultFuture.get();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("CDB", result.get(0).getProduct());

    verify(pricingMapper, times(1)).mapToOffersPricingResponse(request);
  }
}
