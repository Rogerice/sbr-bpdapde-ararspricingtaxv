package com.santander.bp.integration.delegate;

import com.santander.bp.api.OffersAndRatesApiDelegate;
import com.santander.bp.exception.AltairException;
import com.santander.bp.exception.CosmosDbException;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.Error;
import com.santander.bp.model.Error.LevelEnum;
import com.santander.bp.model.Errors;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.service.CosmosDbService;
import com.santander.bp.service.OffersPricingServiceBP82;
import com.santander.bp.service.whitelist.WhitelistService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OffersAndRatesApiDelegateImpl implements OffersAndRatesApiDelegate {

  @Autowired private OffersPricingServiceBP82 offersPricingServiceBP82;

  @Autowired private CosmosDbService cosmosDbService;

  @Autowired private WhitelistService whitelistService;

  @Override
  public CompletableFuture<ResponseEntity<List<OffersPricingResponse>>> offersPost(
      OffersPricingRequest offersPricingRequest, Integer limit, Integer offset) {

    CompletableFuture<ResponseEntity<List<OffersPricingResponse>>> future =
        new CompletableFuture<>();

    try {
      String documentNumber = offersPricingRequest.getDocumentNumber();
      String centerId = offersPricingRequest.getCenterId();

      if (whitelistService.isInWhitelist(documentNumber, centerId)) {
        List<OffersPricingResponse> cosmosOffers =
            cosmosDbService.getOffers(
                offersPricingRequest.getSegment(), offersPricingRequest.getChannel(), "26");

        if (cosmosOffers.isEmpty()) {
          throw new CosmosDbException(
              "NOT_LIST_ERROR",
              "No offers found",
              "No offers were found with the specified criteria in CosmosDB");
        }

        future.complete(ResponseEntity.ok(cosmosOffers));
      } else {
        List<OffersPricingResponse> response =
            offersPricingServiceBP82.processOffers(offersPricingRequest);
        future.complete(ResponseEntity.ok(response));
      }
    } catch (Exception e) {
      Errors errorResponse;
      if (e instanceof CosmosDbException || e instanceof AltairException) {
        int code = ((RestApiException) e).getCode();
        String message = e.getMessage();
        String description = ((AltairException) e).getDescription();

        errorResponse =
            Errors.builder()
                .errors(
                    List.of(
                        Error.builder()
                            // .code(code)
                            .message(message)
                            .level(LevelEnum.ERROR)
                            .description(description)
                            .build()))
                .build();
      } else {
        // Tratamento de erro genérico
        errorResponse =
            Errors.builder()
                .errors(
                    List.of(
                        Error.builder()
                            .code("500")
                            .message("Internal Server Errowrdfdsdsfr")
                            .level(LevelEnum.ERROR)
                            .description(e.getMessage())
                            .build()))
                .build();
      }

      OffersPricingResponse errorPricingResponse =
          OffersPricingResponse.builder().errors(errorResponse.getErrors()).build();

      ResponseEntity<List<OffersPricingResponse>> responseEntity =
          ResponseEntity.status(
                  e instanceof RestApiException
                      ? ((RestApiException) e).getHttpStatus()
                      : HttpStatus.INTERNAL_SERVER_ERROR)
              .body(List.of(errorPricingResponse));

      future.complete(responseEntity);
    }

    return future;
  }
}
