package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.OffersPricingResponseRateTermInner;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PricingEngineServiceTest {

  private PricingEngineService pricingEngineService;

  @BeforeEach
  void setUp() {
    pricingEngineService = new PricingEngineService();
  }

  @Test
  void testEnrichOffersWithMockRates_Success() {
    // Criando oferta simulada
    OffersPricingResponse originalOffer =
        OffersPricingResponse.builder()
            .idAdapter("123")
            .product("Investment")
            .subProductCode("SP001")
            .subProduct("Fixed Income")
            .family("Savings")
            .feeDescription("Fee Test")
            .closingFee(0.5)
            .receivingFee(0.3)
            .messages(List.of("Test message"))
            .build();

    Collection<OffersPricingResponse> enrichedOffers =
        pricingEngineService.enrichOffersWithMockRates(List.of(originalOffer));

    // Verificações
    assertNotNull(enrichedOffers);
    assertEquals(1, enrichedOffers.size());

    OffersPricingResponse enrichedOffer = enrichedOffers.iterator().next();
    assertEquals("123", enrichedOffer.getIdAdapter());
    assertEquals("Investment", enrichedOffer.getProduct());
    assertNotNull(enrichedOffer.getRateTerm());
    assertEquals(2, enrichedOffer.getRateTerm().size());
    assertNotNull(enrichedOffer.getVoucher());
    assertEquals("Mocked Pricing - BP", enrichedOffer.getVoucher());
  }

  @Test
  void testGetLowestTerm_ReturnsCorrectly() {
    Collection<OffersPricingResponseRateTermInner> rateTerms =
        List.of(
            new OffersPricingResponseRateTermInner(180, 12.5),
            new OffersPricingResponseRateTermInner(365, 11.0));

    Integer lowestTerm = pricingEngineService.getLowestTerm(rateTerms);
    assertEquals(180, lowestTerm);
  }

  @Test
  void testGetLowestTerm_EmptyListReturnsNull() {
    Integer lowestTerm = pricingEngineService.getLowestTerm(List.of());
    assertNull(lowestTerm);
  }
}
