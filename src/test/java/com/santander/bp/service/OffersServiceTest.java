package com.santander.bp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.santander.bp.model.OffersPricingResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OffersServiceTest {

  @Mock private CosmosDbService cosmosDbService;

  @InjectMocks private OffersService offersService;

  @BeforeEach
  void setUp() {
    // Inicializa os mocks
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void whenCosmosServiceReturnsData_thenReturnOffers() {
    // Arrange
    OffersPricingResponse mockResponse = new OffersPricingResponse();
    mockResponse.setId("1");
    mockResponse.setProduct("Product A");
    when(cosmosDbService.getOffers(anyString(), anyString(), anyString()))
        .thenReturn(List.of(mockResponse));

    // Act
    List<OffersPricingResponse> response = offersService.getOffers("segment", "channel", "product");

    // Assert
    assertThat(response).isNotEmpty();
    assertThat(response.get(0).getId()).isEqualTo("1");
    assertThat(response.get(0).getProduct()).isEqualTo("Product A");
    verify(cosmosDbService, times(1)).getOffers(anyString(), anyString(), anyString());
  }

  @Test
  void whenCosmosServiceFails_thenFallbackIsCalled() {
    // Act
    List<OffersPricingResponse> response =
        offersService.fallbackGetOffers(
            "segment", "channel", "product", new RuntimeException("Service Unavailable"));

    // Assert
    assertThat(response).isEmpty(); // Deve retornar uma lista vazia pelo fallback
  }

  @Test
  void whenCosmosServiceReturnsEmptyList_thenReturnEmptyList() {
    // Arrange
    when(cosmosDbService.getOffers(anyString(), anyString(), anyString()))
        .thenReturn(Collections.emptyList());

    // Act
    List<OffersPricingResponse> response = offersService.getOffers("segment", "channel", "product");

    // Assert
    assertThat(response).isEmpty();
    verify(cosmosDbService, times(1)).getOffers(anyString(), anyString(), anyString());
  }

  @Test
  void fallbackMethodReturnsEmptyList_whenTriggered() {
    // Act
    List<OffersPricingResponse> response =
        offersService.fallbackGetOffers("segment", "channel", "product", new RuntimeException());

    // Assert
    assertThat(response).isEmpty();
  }

  @Test
  void whenInvalidInputs_thenThrowException() {
    // Arrange
    doThrow(new IllegalArgumentException("Invalid inputs"))
        .when(cosmosDbService)
        .getOffers(eq("invalid"), eq("invalid"), eq("invalid"));
  }
}
