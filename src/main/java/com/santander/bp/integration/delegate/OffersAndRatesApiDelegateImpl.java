package com.santander.bp.integration.delegate;

import com.santander.bp.api.OffersAndRatesApiDelegate;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.Error.LevelEnum;
import com.santander.bp.model.Errors;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.ResponseWrapper;
import com.santander.bp.service.CosmosDbService;
import com.santander.bp.service.OffersPricingServiceBP82;
import com.santander.bp.service.whitelist.WhitelistService;
import com.santander.bp.util.ErrorBuilderUtil;
import com.santander.bp.util.ResponseBuilderUtil;
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
      OffersPricingRequest offersPricingRequest) {

    CompletableFuture<ResponseEntity<ResponseWrapper>> future = new CompletableFuture<>();

    try {
      String documentNumber = offersPricingRequest.getDocumentNumber();
      String centerId = offersPricingRequest.getCenterId();

      if (whitelistService.isInWhitelist(documentNumber, centerId)) {
        // Processamento para clientes da whitelist: ofertas do CosmosDB
        try {
          List<OffersPricingResponse> cosmosOffers =
              cosmosDbService.getOffers(
                  offersPricingRequest.getSegment(), offersPricingRequest.getChannel(), "26");

          if (cosmosOffers.isEmpty()) {
            // Nenhuma oferta encontrada
            Errors errorResponse =
                ErrorBuilderUtil.buildNotFoundError(
                    "No offers were found with the specified criteria in CosmosDB");
            future.complete(
                ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.NOT_FOUND));
          } else {
            // Sucesso: Retornar as ofertas encontradas
            future.complete(ResponseBuilderUtil.buildSuccessResponse(cosmosOffers));
          }
        } catch (RestApiException e) {
          // Capturando o erro específico do CosmosDbService
          Errors errorResponse =
              ErrorBuilderUtil.buildError(
                  e.getCode(), e.getMessage(), e.getTitle(), LevelEnum.ERROR);
          future.complete(
              ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.NOT_FOUND));
        }
      } else {
        // Processamento para clientes fora da whitelist
        handleNonWhitelistCase(offersPricingRequest, future);
      }
    } catch (RestApiException e) {
      Errors errorResponse =
          ErrorBuilderUtil.buildError(e.getCode(), e.getMessage(), e.getTitle(), LevelEnum.ERROR);
      future.complete(ResponseBuilderUtil.buildErrorResponse(errorResponse, e.getHttpStatus()));
    } catch (Exception e) {
      Errors errorResponse = ErrorBuilderUtil.buildServerError(e.getMessage());
      future.complete(
          ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    return future;
  }

  private void handleNonWhitelistCase(
      OffersPricingRequest offersPricingRequest,
      CompletableFuture<ResponseEntity<ResponseWrapper>> future) {

    try {
      List<OffersPricingResponse> response =
          offersPricingServiceBP82.processOffers(offersPricingRequest);

      if (response.isEmpty()) {
        Errors errorResponse =
            ErrorBuilderUtil.buildError(
                "NOT_FOUND",
                "No offers found",
                "No offers were found for the given criteria.",
                LevelEnum.INFO);
        future.complete(
            ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.NOT_FOUND));
      } else {
        future.complete(ResponseBuilderUtil.buildSuccessResponse(response));
      }
    } catch (RestApiException e) {
      // Capturando RestApiException do processo não-whitelist
      Errors errorResponse =
          ErrorBuilderUtil.buildError(e.getCode(), e.getMessage(), e.getTitle(), LevelEnum.ERROR);
      future.complete(
          ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.BAD_REQUEST));
    } catch (Exception e) {
      Errors errorResponse = ErrorBuilderUtil.buildServerError(e.getMessage());
      future.complete(
          ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
    }
  }
}
