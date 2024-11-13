package com.santander.bp.integration.delegate;

import com.santander.bp.api.OffersAndRatesApiDelegate;
import com.santander.bp.exception.AltairException;
import com.santander.bp.model.Error;
import com.santander.bp.model.Error.LevelEnum;
import com.santander.bp.model.Errors;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.ResponseWrapper;
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
  public CompletableFuture<ResponseEntity<ResponseWrapper>> offersPost(
      OffersPricingRequest offersPricingRequest, Integer limit, Integer offset) {

    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return handleOffersRequest(offersPricingRequest);
          } catch (AltairException e) {
            return buildErrorResponse(
                e.getCode(), e.getMessage(), e.getDescription(), HttpStatus.BAD_REQUEST);
          } catch (Exception e) {
            return buildErrorResponse(
                "500", "Internal Server Error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
          }
        });
  }

  private ResponseEntity<ResponseWrapper> handleOffersRequest(
      OffersPricingRequest offersPricingRequest) {
    String documentNumber = offersPricingRequest.getDocumentNumber();
    String centerId = offersPricingRequest.getCenterId();

    List<OffersPricingResponse> offers;

    if (whitelistService.isInWhitelist(documentNumber, centerId)) {
      offers =
          cosmosDbService.getOffers(
              offersPricingRequest.getSegment(), offersPricingRequest.getChannel(), "26");

      if (offers.isEmpty()) {
        return buildErrorResponse(
            "NOT_LIST_ERROR",
            "No offers found",
            "No offers were found with the specified criteria in CosmosDB",
            HttpStatus.NOT_FOUND);
      }
    } else {
      offers = offersPricingServiceBP82.processOffers(offersPricingRequest);
    }

    return buildSuccessResponse(offers);
  }

  private ResponseEntity<ResponseWrapper> buildErrorResponse(
      String code, String message, String description, HttpStatus status) {
    Errors errorResponse =
        Errors.builder()
            .errors(
                List.of(
                    Error.builder()
                        .code(code)
                        .message(message)
                        .level(LevelEnum.ERROR)
                        .description(description)
                        .build()))
            .build();

    ResponseWrapper responseWrapper =
        ResponseWrapper.builder().errors(errorResponse.getErrors()).build();

    return ResponseEntity.status(status).body(responseWrapper);
  }

  private ResponseEntity<ResponseWrapper> buildSuccessResponse(List<OffersPricingResponse> offers) {
    ResponseWrapper responseWrapper = ResponseWrapper.builder().data(offers).build();

    return ResponseEntity.ok(responseWrapper);
  }
}
