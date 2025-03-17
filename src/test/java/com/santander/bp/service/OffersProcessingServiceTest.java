package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.service.whitelist.WhitelistService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class OffersProcessingServiceTest {

  @Mock private OffersPricingServiceBP82 offersPricingServiceBP82;
  @Mock private OffersService offersService;
  @Mock private WhitelistService whitelistService;
  @Mock private OffersMapperBP82 offersMapperBP82;

  private OffersProcessingService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    service =
        new OffersProcessingService(
            offersPricingServiceBP82, offersService, whitelistService, offersMapperBP82);
  }

  @Test
  void testProcessOffers_Whitelist_Success() throws ExecutionException, InterruptedException {
    OffersPricingRequest request = mock(OffersPricingRequest.class);
    when(request.getDocumentType()).thenReturn("CPF");
    when(request.getDocumentNumber()).thenReturn("12345678900");
    when(request.getSegment()).thenReturn("SEGMENTO_A");
    when(request.getChannel()).thenReturn("CANAL_1");

    when(whitelistService.isInWhitelist("CPF", "12345678900")).thenReturn(true);
    when(offersService.getOffers("SEGMENTO_A", "CANAL_1", "26"))
        .thenReturn(List.of(new OffersPricingResponse()));
    when(offersService.enrichOffersWithPricing(anyList()))
        .thenReturn(List.of(new OffersPricingResponse()));

    CompletableFuture<List<OffersPricingResponse>> futureResponse = service.processOffers(request);
    List<OffersPricingResponse> result = futureResponse.get();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    verify(offersService).getOffers("SEGMENTO_A", "CANAL_1", "26");
    verify(offersService).enrichOffersWithPricing(anyList());
  }

  @Test
  void testProcessOffers_NonWhitelist_Success() throws ExecutionException, InterruptedException {
    OffersPricingRequest request = mock(OffersPricingRequest.class);
    when(request.getDocumentType()).thenReturn("CPF");
    when(request.getDocumentNumber()).thenReturn("12345678900");
    // when(request.getSegment()).thenReturn("SEGMENTO_A");
    // when(request.getChannel()).thenReturn("CANAL_1");

    when(whitelistService.isInWhitelist("CPF", "12345678900")).thenReturn(false);
    when(offersPricingServiceBP82.processOffers(request))
        .thenReturn(List.of(new OffersPricingResponse()));

    CompletableFuture<List<OffersPricingResponse>> futureResponse = service.processOffers(request);
    List<OffersPricingResponse> result = futureResponse.get();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    verify(offersPricingServiceBP82).processOffers(request);
  }

  @Test
  void testProcessOffers_Whitelist_NoOffers() {
    OffersPricingRequest request = mock(OffersPricingRequest.class);
    when(request.getDocumentType()).thenReturn("CPF");
    when(request.getDocumentNumber()).thenReturn("12345678900");
    when(request.getSegment()).thenReturn("SEGMENTO_A");
    when(request.getChannel()).thenReturn("CANAL_1");

    when(whitelistService.isInWhitelist("CPF", "12345678900")).thenReturn(true);
    when(offersService.getOffers("SEGMENTO_A", "CANAL_1", "26")).thenReturn(List.of());

    // Captura a exceção CompletionException
    CompletionException completionException =
        assertThrows(CompletionException.class, () -> service.processOffers(request).join());

    // Extrai a causa (que é a RestApiException) e faz as asserções corretas
    Throwable cause = completionException.getCause();
    assertTrue(cause instanceof RestApiException);

    RestApiException exception = (RestApiException) cause;
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals("NOT_FOUND", exception.getCode());
    assertEquals("Nenhuma oferta encontrada", exception.getTitle());
  }

  @Test
  void testProcessOffers_NonWhitelist_NoOffers() {
    OffersPricingRequest request = mock(OffersPricingRequest.class);
    when(request.getDocumentType()).thenReturn("CPF");
    when(request.getDocumentNumber()).thenReturn("12345678900");
    // when(request.getSegment()).thenReturn("SEGMENTO_A");
    // when(request.getChannel()).thenReturn("CANAL_1");

    when(whitelistService.isInWhitelist("CPF", "12345678900")).thenReturn(false);
    when(whitelistService.isAgencyInWhitelist(any())).thenReturn(false);
    when(offersPricingServiceBP82.processOffers(request)).thenReturn(List.of());

    // Captura a exceção CompletionException
    CompletionException completionException =
        assertThrows(CompletionException.class, () -> service.processOffers(request).join());

    // Extrai a causa (que é a RestApiException) e faz as asserções corretas
    Throwable cause = completionException.getCause();
    assertTrue(cause instanceof RestApiException);

    RestApiException exception = (RestApiException) cause;
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals("NOT_FOUND", exception.getCode());
    assertEquals("Nenhuma oferta encontrada", exception.getTitle());
  }
}
