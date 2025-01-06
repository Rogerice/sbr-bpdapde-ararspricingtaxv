package com.santander.bp.integration.delegate;

import com.santander.bp.api.OffersAndRatesApiDelegate;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.Error.LevelEnum;
import com.santander.bp.model.Errors;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.ResponseWrapper;
import com.santander.bp.service.OffersPricingServiceBP82;
import com.santander.bp.service.OffersService;
import com.santander.bp.service.whitelist.WhitelistService;
import com.santander.bp.util.ErrorBuilderUtil;
import com.santander.bp.util.ResponseBuilderUtil;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Generated
@Slf4j
@Service
public class OffersAndRatesApiDelegateImpl implements OffersAndRatesApiDelegate {

  @Autowired private OffersPricingServiceBP82 offersPricingServiceBP82;

  @Autowired private OffersService offersService;

  @Autowired private WhitelistService whitelistService;

  @Override
  public CompletableFuture<ResponseEntity<ResponseWrapper>> offersPost(
      OffersPricingRequest offersPricingRequest) {

    log.info("Recebendo solicitação de ofertas com dados: {}", offersPricingRequest);

    CompletableFuture<ResponseEntity<ResponseWrapper>> future = new CompletableFuture<>();

    try {
      if (isClientInWhitelist(offersPricingRequest)) {
        log.info(
            "Cliente com documentType: {}, documentNumber: {} está na whitelist.",
            offersPricingRequest.getDocumentType(),
            offersPricingRequest.getDocumentNumber());

        processWhitelistOffers(offersPricingRequest, future);
      } else {
        log.info(
            "Cliente com documentType: {}, documentNumber: {} não está na whitelist. Processando pelo fluxo principal.",
            offersPricingRequest.getDocumentType(),
            offersPricingRequest.getDocumentNumber());

        processNonWhitelistOffers(offersPricingRequest, future);
      }
    } catch (RestApiException e) {
      log.error("Erro de API ao processar solicitação: {}", e.getMessage(), e);
      handleRestApiException(e, future);
    } catch (Exception e) {
      log.error("Erro inesperado ao processar solicitação: {}", e.getMessage(), e);
      handleUnexpectedException(e, future);
    }

    return future;
  }

  private void processWhitelistOffers(
      OffersPricingRequest offersPricingRequest,
      CompletableFuture<ResponseEntity<ResponseWrapper>> future) {
    try {
      log.debug("Buscando ofertas para cliente na whitelist.");
      List<OffersPricingResponse> cosmosOffers =
          offersService.getOffers(
              offersPricingRequest.getSegment(), offersPricingRequest.getChannel(), "26");

      if (cosmosOffers.isEmpty()) {
        log.warn("Nenhuma oferta encontrada no CosmosDB para os critérios especificados.");
        Errors errorResponse =
            ErrorBuilderUtil.buildNotFoundError(
                "Nenhuma oferta foi encontrada com os critérios especificados no CosmosDB");
        future.complete(
            ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.NOT_FOUND));
      } else {
        log.info("Ofertas encontradas no CosmosDB: {}", cosmosOffers);
        future.complete(ResponseBuilderUtil.buildSuccessResponse(cosmosOffers));
      }
    } catch (RestApiException e) {
      log.error("Erro de API ao buscar ofertas no CosmosDB: {}", e.getMessage(), e);
      handleRestApiException(e, future);
    }
  }

  private void processNonWhitelistOffers(
      OffersPricingRequest offersPricingRequest,
      CompletableFuture<ResponseEntity<ResponseWrapper>> future) {
    try {
      log.debug("Buscando ofertas para cliente fora da whitelist.");

      List<OffersPricingResponse> responseList =
          offersPricingServiceBP82.processOffers(offersPricingRequest);

      if (responseList.isEmpty()) {
        log.warn("Nenhuma oferta encontrada para os parâmetros fornecidos.");
        Errors errorResponse =
            ErrorBuilderUtil.buildError(
                "NOT_FOUND",
                "Nenhuma oferta encontrada",
                "Nenhuma oferta foi encontrada para os parâmetros fornecidos",
                LevelEnum.INFO);
        future.complete(
            ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.NOT_FOUND));
      } else {
        log.info("Ofertas encontradas: {}", responseList);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(responseList);
        future.complete(ResponseEntity.ok(responseWrapper));
      }
    } catch (RestApiException e) {
      log.error(
          "Erro de API ao processar ofertas para cliente fora da whitelist: {}", e.getMessage(), e);
      handleRestApiException(e, future);
    } catch (Exception e) {
      log.error(
          "Erro inesperado ao processar ofertas para cliente fora da whitelist: {}",
          e.getMessage(),
          e);
      handleUnexpectedException(e, future);
    }
  }

  private boolean isClientInWhitelist(OffersPricingRequest offersPricingRequest) {
    log.debug(
        "Verificando se o cliente está na whitelist. DocumentType: {}, DocumentNumber: {}, CenterId: {}",
        offersPricingRequest.getDocumentType(),
        offersPricingRequest.getDocumentNumber(),
        offersPricingRequest.getCenterId());

    boolean isInWhitelist =
        whitelistService.isInWhitelist(
                offersPricingRequest.getDocumentType(), offersPricingRequest.getDocumentNumber())
            || whitelistService.isAgencyInWhitelist(offersPricingRequest.getCenterId());

    log.debug("Resultado da verificação de whitelist: {}", isInWhitelist);
    return isInWhitelist;
  }

  private void handleRestApiException(
      RestApiException e, CompletableFuture<ResponseEntity<ResponseWrapper>> future) {
    log.error("Erro de API capturado: {}", e.getMessage(), e);
    Errors errorResponse =
        ErrorBuilderUtil.buildError(e.getCode(), e.getMessage(), e.getTitle(), LevelEnum.ERROR);
    future.complete(ResponseBuilderUtil.buildErrorResponse(errorResponse, e.getHttpStatus()));
  }

  private void handleUnexpectedException(
      Exception e, CompletableFuture<ResponseEntity<ResponseWrapper>> future) {
    log.error("Erro inesperado capturado: {}", e.getMessage(), e);
    Errors errorResponse = ErrorBuilderUtil.buildServerError(e.getMessage());
    future.complete(
        ResponseBuilderUtil.buildErrorResponse(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
