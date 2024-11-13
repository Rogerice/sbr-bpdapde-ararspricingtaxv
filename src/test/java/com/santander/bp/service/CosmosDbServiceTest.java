package com.santander.bp.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.santander.bp.app.mapper.CosmosDbMapper;
import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.repository.OffersCosmosDb;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CosmosDbServiceTest {

  @Mock private OffersCosmosDb offerRepository;

  @Mock private CosmosDbMapper cosmosDbMapper;

  @InjectMocks private CosmosDbService cosmosDbService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetOffers_OffersAvailable() {

    OfferCosmosDTO offer = new OfferCosmosDTO();
    offer.setId("offer-1");
    offer.setProduct("26");
    when(offerRepository.findOffers(any(), any(), any())).thenReturn(List.of(offer));

    OffersPricingResponse responseMock = new OffersPricingResponse();
    responseMock.setId("offer-1");
    when(cosmosDbMapper.mapToOfferResponseDTO(any())).thenReturn(responseMock); // Mockando o mapper

    List<OffersPricingResponse> response = cosmosDbService.getOffers("ALL", "ONLINE", "26");

    assertEquals(1, response.size());
    assertEquals("offer-1", response.get(0).getId());
  }

  @Test
  void testGetOffers_NoOffersFound() {

    when(offerRepository.findOffers(any(), any(), any())).thenReturn(Collections.emptyList());

    List<OffersPricingResponse> offers = cosmosDbService.getOffers("ALL", "ONLINE", "26");
    assertNotNull(offers);
  }
}
