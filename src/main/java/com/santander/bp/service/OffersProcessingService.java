package com.santander.bp.service;

import com.santander.bp.app.mapper.OffersMapperBP82; // Adicionamos a injeção do Mapper
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.service.whitelist.WhitelistService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OffersProcessingService {

  private final OffersPricingServiceBP82 offersPricingServiceBP82;
  private final OffersService offersService;
  private final WhitelistService whitelistService;

  public OffersProcessingService(
      OffersPricingServiceBP82 offersPricingServiceBP82,
      OffersService offersService,
      WhitelistService whitelistService,
      OffersMapperBP82 offersMapperBP82) {
    this.offersPricingServiceBP82 = offersPricingServiceBP82;
    this.offersService = offersService;
    this.whitelistService = whitelistService;
  }

  public CompletableFuture<List<OffersPricingResponse>> processOffers(
      OffersPricingRequest request) {
    return CompletableFuture.supplyAsync(
            () -> {
              log.info("🔍Validando whitelist...");

              boolean isDocumentInWhitelist =
                  whitelistService.isInWhitelist(
                      request.getDocumentType(), request.getDocumentNumber());

              boolean isAgencyInWhitelist =
                  !isDocumentInWhitelist
                      && whitelistService.isAgencyInWhitelist(request.getCenterId());

              log.info(
                  "Resultado da validação de whitelist - Documento: {}, Agência: {}",
                  isDocumentInWhitelist,
                  isAgencyInWhitelist);

              if (isDocumentInWhitelist || isAgencyInWhitelist) {
                return processWhitelistOffers(request);
              } else {
                return null;
              }
            })
        .thenCompose(
            result -> {
              if (result != null) {
                return CompletableFuture.completedFuture(result);
              } else {
                return processNonWhitelistOffers(request);
              }
            });
  }

  private List<OffersPricingResponse> processWhitelistOffers(OffersPricingRequest request) {
    log.info("Cliente está na whitelist. Buscando ofertas no CosmosDB...");

    List<OffersPricingResponse> cosmosOffers =
        offersService.getOffers(request.getSegment(), request.getChannel(), "26");

    if (cosmosOffers.isEmpty()) {
      log.warn(" Nenhuma oferta encontrada no CosmosDB.");
      throw new RestApiException(
          HttpStatus.NOT_FOUND,
          "NOT_FOUND",
          "Nenhuma oferta encontrada",
          "Nenhuma oferta foi encontrada com os critérios especificados no CosmosDB.",
          null);
    }

    log.info("Enriquecendo ofertas com Pricing...");
    return offersService.enrichOffersWithPricing(cosmosOffers);
  }

  private CompletableFuture<List<OffersPricingResponse>> processNonWhitelistOffers(
      OffersPricingRequest request) {

    log.info("Cliente NÃO está na whitelist. Buscando ofertas via BP82...");

    return CompletableFuture.supplyAsync(
        () -> {
          log.info("Chamando `processOffers` do `OffersPricingServiceBP82`...");
          List<OffersPricingResponse> responseList =
              offersPricingServiceBP82.processOffers(request);

          log.info(
              "Processo concluído via `OffersPricingServiceBP82`. Tamanho da lista: {}",
              responseList.size());

          if (responseList.isEmpty()) {
            log.warn("Nenhuma oferta encontrada para os parâmetros fornecidos.");
            throw new RestApiException(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                "Nenhuma oferta encontrada",
                "Nenhuma oferta foi encontrada para os critérios especificados.",
                null);
          }

          return responseList;
        });
  }
}
