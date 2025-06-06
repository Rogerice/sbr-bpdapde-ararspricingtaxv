package com.santander.bp.integration.delegate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.santander.bp.api.OffersAndRatesApiDelegate;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.ErrorLevel;
import com.santander.bp.model.Errors;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.ResponseWrapper;
import com.santander.bp.service.OffersProcessingService;
import com.santander.bp.util.ErrorBuilderUtil;
import com.santander.bp.util.ResponseBuilderUtil;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Generated
public class OffersAndRatesApiDelegateImpl implements OffersAndRatesApiDelegate {

    private final OffersProcessingService offersProcessingService;

    public OffersAndRatesApiDelegateImpl(OffersProcessingService offersProcessingService) {
        this.offersProcessingService = offersProcessingService;
    }

    @Override
    public CompletableFuture<ResponseEntity<ResponseWrapper>> offersPost(OffersPricingRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Recebendo requisição no offersPost: {}", request);

        return offersProcessingService.processOffers(request)
                .thenApply(responseList -> {
                    ResponseWrapper responseWrapper = new ResponseWrapper();
                    responseWrapper.setData(responseList);

                    long totalTime = System.currentTimeMillis();
                    log.info("Tempo total de processamento: {} ms", (totalTime - startTime));

                    return ResponseEntity.ok(responseWrapper);
                })
                .exceptionally(ex -> handleException(ex));
    }


    private ResponseEntity<ResponseWrapper> handleException(Throwable ex) {
        if (ex.getCause() instanceof RestApiException restApiException) {
            log.error("Erro de API: {}", restApiException.getMessage(), restApiException);
            Errors errorResponse = ErrorBuilderUtil.buildError(
                    restApiException.getCode(),
                    restApiException.getMessage(),
                    restApiException.getTitle(),
                    ErrorLevel.ERROR
            );

            return ResponseBuilderUtil.buildErrorResponse(errorResponse, restApiException.getHttpStatus());
        }

        log.error("Erro inesperado na API: {}", ex.getMessage(), ex);
        Errors errorResponse = ErrorBuilderUtil.buildServerError(ex.getMessage());
        return ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
