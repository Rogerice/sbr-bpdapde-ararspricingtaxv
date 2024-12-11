package com.santander.bp.integration.delegate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.santander.bp.model.Error;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.ResponseWrapper;
import com.santander.bp.service.CosmosDbService;
import com.santander.bp.service.OffersPricingServiceBP82;
import com.santander.bp.service.OffersService;
import com.santander.bp.service.whitelist.WhitelistService;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class OffersAndRatesApiDelegateImplTest {

  @Mock private OffersPricingServiceBP82 offersPricingServiceBP82;

  @Mock private CosmosDbService cosmosDbService;

  @Mock private WhitelistService whitelistService;

  @Mock private OffersService offersService;

  @InjectMocks private OffersAndRatesApiDelegateImpl offersAndRatesApiDelegate;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @SuppressWarnings("deprecation")
  @Test
  void testOffersPost_ClientInWhitelist_NoOffersFound() {
    OffersPricingRequest request = new OffersPricingRequest();
    request.setDocumentNumber("12345678909");
    request.setCenterId("002");
    request.setSegment("ALL");
    request.setChannel("ONLINE");
    request.setDocumentType("CPF");

    when(whitelistService.isInWhitelist(any(), any())).thenReturn(true);
    when(offersService.getOffers(any(), any(), any())).thenReturn(Collections.emptyList());

    CompletableFuture<ResponseEntity<ResponseWrapper>> responseFuture =
        offersAndRatesApiDelegate.offersPost(request);
    ResponseEntity<ResponseWrapper> response = responseFuture.join();

    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    Error errors = response.getBody().getErrors().get(0);
    assertEquals("NOT_FOUND", errors.getCode());
    assertEquals("No offers found", errors.getMessage());
  }

  @SuppressWarnings("deprecation")
  @Test
  void testOffersPost_ClientInWhitelist_OffersFound() {
    OffersPricingRequest request = new OffersPricingRequest();
    request.setDocumentNumber("12345678909");
    request.setCenterId("002");
    request.setSegment("ALL");
    request.setChannel("ONLINE");
    request.setDocumentType("CPF");

    OffersPricingResponse offer = new OffersPricingResponse();
    offer.setId("offer-1");
    when(whitelistService.isInWhitelist(any(), any())).thenReturn(true);
    when(offersService.getOffers(any(), any(), any())).thenReturn(List.of(offer));

    CompletableFuture<ResponseEntity<ResponseWrapper>> responseFuture =
        offersAndRatesApiDelegate.offersPost(request);
    ResponseEntity<ResponseWrapper> response = responseFuture.join();

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getData().contains(offer));
  }

  @SuppressWarnings("deprecation")
  @Test
  void testOffersPost_ClientNotInWhitelist_NoOffersFound() {
    OffersPricingRequest request = new OffersPricingRequest();
    request.setDocumentNumber("98765432100");
    request.setCenterId("003");
    request.setSegment("ALL");
    request.setChannel("APP");
    request.setDocumentType("CPF");

    when(whitelistService.isInWhitelist(any(), any())).thenReturn(false);
    when(offersPricingServiceBP82.processOffers(any())).thenReturn(Collections.emptyList());

    CompletableFuture<ResponseEntity<ResponseWrapper>> responseFuture =
        offersAndRatesApiDelegate.offersPost(request);
    ResponseEntity<ResponseWrapper> response = responseFuture.join();

    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    Error errors = response.getBody().getErrors().get(0);
    assertEquals("NOT_FOUND", errors.getCode());
    assertEquals("Nenhuma oferta encontrada", errors.getMessage());
  }
}
