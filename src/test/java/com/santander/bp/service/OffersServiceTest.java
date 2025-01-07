package com.santander.bp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import com.santander.bp.model.OffersPricingResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OffersServiceTest {

  @MockBean private CosmosDbService cosmosDbService;

  @Autowired private OffersService offersService;

  @Test
  void whenCosmosServiceFails_thenFallbackIsCalled() {
    Mockito.when(cosmosDbService.getOffers(anyString(), anyString(), anyString()))
        .thenThrow(new RuntimeException("Service Unavailable"));

    List<OffersPricingResponse> response = offersService.getOffers("segment", "channel", "product");

    assertThat(response).isEmpty();
  }
}
