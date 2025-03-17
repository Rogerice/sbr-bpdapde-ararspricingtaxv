package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.santander.bp.model.OffersPricingResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OffersServiceTest {

  @Mock private CosmosDbService cosmosDbService;
  @Mock private PricingEngineService pricingEngineService;

  private OffersService offersService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    offersService = new OffersService(cosmosDbService, pricingEngineService);
  }

  @Test
  void testGetOffers_Success() {
    List<OffersPricingResponse> offersFromDb = List.of(new OffersPricingResponse());
    when(cosmosDbService.getOffers("SEGMENT_1", "CHANNEL_A", "PRODUCT_X")).thenReturn(offersFromDb);

    List<OffersPricingResponse> enrichedOffers = List.of(new OffersPricingResponse());
    when(pricingEngineService.enrichOffersWithMockRates(offersFromDb)).thenReturn(enrichedOffers);

    List<OffersPricingResponse> result =
        offersService.getOffers("SEGMENT_1", "CHANNEL_A", "PRODUCT_X");

    assertNotNull(result);
    assertEquals(enrichedOffers, result);
    verify(cosmosDbService).getOffers("SEGMENT_1", "CHANNEL_A", "PRODUCT_X");
    verify(pricingEngineService).enrichOffersWithMockRates(offersFromDb);
  }

  @Test
  void testEnrichOffersWithPricing_Success() {
    List<OffersPricingResponse> offers = List.of(new OffersPricingResponse());

    List<OffersPricingResponse> enrichedOffers = List.of(new OffersPricingResponse());
    when(pricingEngineService.enrichOffersWithMockRates(offers)).thenReturn(enrichedOffers);

    List<OffersPricingResponse> result = offersService.enrichOffersWithPricing(offers);

    assertNotNull(result);
    assertEquals(enrichedOffers, result);
    verify(pricingEngineService).enrichOffersWithMockRates(offers);
  }
}
