package com.santander.bp.integration.delegate;

import com.santander.bp.api.OffersAndRatesApiDelegate;
import com.santander.bp.exception.AltairException;
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
          future.complete(
              ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body(
                      List.of(
                          OffersPricingResponse.builder()
                              .errors(
                                  List.of(
                                      Error.builder()
                                          .code("NOT_LIST_ERROR")
                                          .message("No offers found")
                                          .level(LevelEnum.INFO)
                                          .description(
                                              "No offers were found with the specified criteria in CosmosDB")
                                          .build()))
                              .build())));
        } else {
          future.complete(ResponseEntity.ok(cosmosOffers));
        }
      } else {
        List<OffersPricingResponse> response =
            offersPricingServiceBP82.processOffers(offersPricingRequest);
        future.complete(ResponseEntity.ok(response));
      }
    } catch (AltairException e) {
      Errors errorResponse =
          Errors.builder()
              .errors(
                  List.of(
                      Error.builder()
                          .code(e.getCode())
                          .message(e.getMessage())
                          .level(LevelEnum.ERROR)
                          .description(e.getDescription())
                          .build()))
              .build();

      OffersPricingResponse errorPricingResponse =
          OffersPricingResponse.builder().errors(errorResponse.getErrors()).build();

      ResponseEntity<List<OffersPricingResponse>> responseEntity =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(errorPricingResponse));

      future.complete(responseEntity);
    } catch (Exception e) {
      Errors errorResponse =
          Errors.builder()
              .errors(
                  List.of(
                      Error.builder()
                          .code("500")
                          .message("Internal Server Error")
                          .level(LevelEnum.ERROR)
                          .description(e.getMessage())
                          .build()))
              .build();

      OffersPricingResponse errorPricingResponse =
          OffersPricingResponse.builder().errors(errorResponse.getErrors()).build();

      ResponseEntity<List<OffersPricingResponse>> responseEntity =
          ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(List.of(errorPricingResponse));

      future.complete(responseEntity);
    }

    return future;
  }
}
