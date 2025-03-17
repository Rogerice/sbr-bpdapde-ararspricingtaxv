package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.santander.bp.app.mapper.CosmosDbMapper;
import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.repository.OffersCosmosDbRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class CosmosDbServiceTest {

  @Mock private OffersCosmosDbRepository offerRepository;

  @Mock private CosmosDbMapper cosmosDbMapper;

  @InjectMocks private CosmosDbService cosmosDbService;

  private static final Logger logger = LoggerFactory.getLogger(CosmosDbService.class);

  private List<OfferCosmosDTO> mockOffers;
  private List<OffersPricingResponse> mockResponses;

  @BeforeEach
  void setUp() {
    OfferCosmosDTO offerDTO = new OfferCosmosDTO();
    offerDTO.setCdSegm("001");
    offerDTO.setChannelCode("APP");
    offerDTO.setProduct("INVEST");
    mockOffers = List.of(offerDTO);

    OffersPricingResponse pricingResponse = new OffersPricingResponse();
    mockResponses = List.of(pricingResponse);
  }

  @Test
  void testGetOffers_Success() {
    when(offerRepository.findOffers("001", "APP", "INVEST")).thenReturn(mockOffers);
    when(cosmosDbMapper.mapOffers(mockOffers)).thenReturn(mockResponses);

    List<OffersPricingResponse> result = cosmosDbService.getOffers("001", "APP", "INVEST");

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    verify(offerRepository, times(1)).findOffers("001", "APP", "INVEST");
    verify(cosmosDbMapper, times(1)).mapOffers(mockOffers);
  }

  @Test
  void testGetOffers_EmptyResult() {
    when(offerRepository.findOffers("001", "APP", "INVEST")).thenReturn(List.of());

    List<OffersPricingResponse> result = cosmosDbService.getOffers("001", "APP", "INVEST");

    assertTrue(result.isEmpty());
    verify(offerRepository, times(1)).findOffers("001", "APP", "INVEST");
    verifyNoInteractions(cosmosDbMapper);
  }

  @Test
  void testGetOffers_NullResult() {
    when(offerRepository.findOffers("001", "APP", "INVEST")).thenReturn(null);

    List<OffersPricingResponse> result = cosmosDbService.getOffers("001", "APP", "INVEST");

    assertTrue(result.isEmpty());
    verify(offerRepository, times(1)).findOffers("001", "APP", "INVEST");
    verifyNoInteractions(cosmosDbMapper);
  }
}
