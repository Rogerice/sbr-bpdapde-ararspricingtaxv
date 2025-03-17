package com.santander.bp.integration.delegate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.OffersPricingResponseRateTermInner;
import com.santander.bp.model.ResponseWrapper;
import com.santander.bp.service.OffersProcessingService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class OffersAndRatesApiDelegateImplTest {

  @Mock private OffersProcessingService offersProcessingService;

  @InjectMocks private OffersAndRatesApiDelegateImpl offersAndRatesApiDelegate;

  private OffersPricingRequest request;

  @BeforeEach
  void setUp() {
    request = new OffersPricingRequest();
  }

  @Test
  void testOffersPost_Success() {
    List<OffersPricingResponse> mockResponse =
        List.of(
            new OffersPricingResponse()
                .product("INVEST")
                .subProductCode("SUB1")
                .subProduct("sub produto SUB1")
                .family("FAMILIA1")
                .minApplicationValue(1000.00)
                .minRedemptionValue(500.00)
                .minBalance(200.00)
                .closingFee(0.05)
                .receivingFee(0.02)
                .feeDescription("Taxa Administrativa")
                .rateTerm(
                    List.of(
                        new OffersPricingResponseRateTermInner().term(30).rate(0.03),
                        new OffersPricingResponseRateTermInner().term(60).rate(0.04)))
                .messages(List.of("Mensagem 1", "Mensagem 2")),
            new OffersPricingResponse()
                .product("FUNDO")
                .subProductCode("SUB2")
                .subProduct("sub produto SUB2")
                .family("FAMILIA2")
                .minApplicationValue(500.00)
                .minRedemptionValue(250.00)
                .minBalance(100.00)
                .closingFee(0.03)
                .receivingFee(0.01)
                .feeDescription("Taxa de Gestão")
                .rateTerm(List.of(new OffersPricingResponseRateTermInner().term(90).rate(0.05)))
                .messages(List.of("Mensagem 3")));

    when(offersProcessingService.processOffers(any()))
        .thenReturn(CompletableFuture.completedFuture(mockResponse));

    CompletableFuture<ResponseEntity<ResponseWrapper>> futureResponse =
        offersAndRatesApiDelegate.offersPost(request);
    ResponseEntity<ResponseWrapper> response = futureResponse.join();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(mockResponse, response.getBody().getData());
  }

  @Test
  void testOffersPost_RestApiException() {
    RestApiException apiException =
        new RestApiException(
            HttpStatus.BAD_REQUEST, "ERROR_CODE", "Título do erro", "Detalhes do erro", null);
    when(offersProcessingService.processOffers(any()))
        .thenReturn(CompletableFuture.failedFuture(apiException));

    CompletableFuture<ResponseEntity<ResponseWrapper>> futureResponse =
        offersAndRatesApiDelegate.offersPost(request);
    ResponseEntity<ResponseWrapper> response = futureResponse.join();

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testOffersPost_UnhandledException() {
    RuntimeException unexpectedException = new RuntimeException("Erro inesperado");
    when(offersProcessingService.processOffers(any()))
        .thenReturn(CompletableFuture.failedFuture(unexpectedException));

    CompletableFuture<ResponseEntity<ResponseWrapper>> futureResponse =
        offersAndRatesApiDelegate.offersPost(request);
    ResponseEntity<ResponseWrapper> response = futureResponse.join();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
