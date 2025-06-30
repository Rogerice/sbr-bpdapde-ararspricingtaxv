package com.santander.bp.service;

import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.service.whitelist.WhitelistService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Generated
public class OffersProcessingService {

  private final OffersPricingServiceBP82 offersPricingServiceBP82;
  private final OffersService offersService;
  private final WhitelistService whitelistService;

  public OffersProcessingService(
      OffersPricingServiceBP82 offersPricingServiceBP82,
      OffersService offersService,
      WhitelistService whitelistService) {
    this.offersPricingServiceBP82 = offersPricingServiceBP82;
    this.offersService = offersService;
    this.whitelistService = whitelistService;
  }

  public CompletableFuture<List<OffersPricingResponse>> processOffers(
      OffersPricingRequest request) {
    return CompletableFuture.supplyAsync(
            () -> {
              log.info("Validando whitelist para documento: {}", request.getDocumentNumber());

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
                // Fluxo Whitelist
                return processWhitelistOffers(request);
              } else {
                // Deixa o próximo estágio da pipeline decidir
                return null;
              }
            })
        .thenCompose(
            result -> {
              if (result != null) {
                // Se o resultado não for nulo, significa que o fluxo Whitelist foi executado
                return CompletableFuture.completedFuture(result);
              } else {
                // Resultado é nulo, então executa o fluxo não-whitelist (legado)
                return processNonWhitelistOffers(request);
              }
            });
  }

  /**
   * Processa o fluxo para clientes na whitelist. Busca ofertas no CosmosDB e as enriquece com o
   * serviço de Pricing.
   */
  private List<OffersPricingResponse> processWhitelistOffers(OffersPricingRequest request) {
    log.info("Cliente na whitelist. Buscando ofertas no CosmosDB...");

    // Passo 1: Buscar ofertas base
    List<OffersPricingResponse> cosmosOffers =
        offersService.getOffers(request.getSegment(), request.getChannel(), "26");

    if (cosmosOffers.isEmpty()) {
      log.warn("Nenhuma oferta encontrada no CosmosDB para os critérios fornecidos.");
      throw new RestApiException(
          HttpStatus.NOT_FOUND,
          "NOT_FOUND",
          "Nenhuma oferta encontrada",
          "Nenhuma oferta foi encontrada com os critérios especificados no CosmosDB.",
          null);
    }

    log.info("{} ofertas encontradas no Cosmos. Enriquecendo com Pricing...", cosmosOffers.size());
    // Passo 2: Enriquecer ofertas com o serviço de pricing.
    // **Ajuste aqui para passar o 'apiRequest' original**
    return offersService.enrichOffersWithPricing(request, cosmosOffers);
  }

  /**
   * Processa o fluxo para clientes que não estão na whitelist. Chama o serviço legado via Altair
   * MQ.
   */
  private CompletableFuture<List<OffersPricingResponse>> processNonWhitelistOffers(
      OffersPricingRequest request) {
    log.info("Cliente NÃO está na whitelist. Buscando ofertas via BP82 (Altair)...");

    return CompletableFuture.supplyAsync(
        () -> {
          List<OffersPricingResponse> responseList =
              offersPricingServiceBP82.processOffers(request);

          if (responseList.isEmpty()) {
            log.warn("Nenhuma oferta encontrada (via BP82) para os parâmetros fornecidos.");
            throw new RestApiException(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                "Nenhuma oferta encontrada",
                "Nenhuma oferta foi encontrada para os critérios especificados.",
                null);
          }
          log.info("Processo via BP82 concluído. {} ofertas encontradas.", responseList.size());
          return responseList;
        });
  }
}
