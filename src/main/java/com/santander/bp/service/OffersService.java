package com.santander.bp.service;

import com.santander.bp.app.mapper.PricingRequestMapper;
import com.santander.bp.app.mapper.PricingResponseMapper;
import com.santander.bp.client.PricingClient;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import com.santander.bp.model.external.PricingRequest;
import java.util.List;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Generated
public class OffersService {

  private final CosmosDbService cosmosDbService;
  private final PricingClient pricingClient;
  private final PricingRequestMapper pricingRequestMapper;
  private final PricingResponseMapper pricingResponseMapper;

  public OffersService(
      CosmosDbService cosmosDbService,
      PricingClient pricingClient,
      PricingRequestMapper pricingRequestMapper,
      PricingResponseMapper pricingResponseMapper) {
    this.cosmosDbService = cosmosDbService;
    this.pricingClient = pricingClient;
    this.pricingRequestMapper = pricingRequestMapper;
    this.pricingResponseMapper = pricingResponseMapper;
  }

  /**
   * Busca as ofertas base no CosmosDB.
   *
   * @param segment Segmento do cliente.
   * @param channel Canal da requisição.
   * @param product Código do produto.
   * @return Lista de ofertas do CosmosDB.
   */
  public List<OffersPricingResponse> getOffers(String segment, String channel, String product) {
    return cosmosDbService.getOffers(segment, channel, product);
  }

  /**
   * Enriquecedor de ofertas. Ele pega as ofertas do CosmosDB, constrói uma requisição para o
   * serviço de Pricing, chama o serviço e, por fim, mescla os resultados.
   *
   * @param apiRequest A requisição original da API, necessária para dados do cliente.
   * @param cosmosOffers A lista de ofertas base vinda do CosmosDB.
   * @return A lista de ofertas enriquecida com as taxas e condições do serviço de Pricing.
   */
  public List<OffersPricingResponse> enrichOffersWithPricing(
      OffersPricingRequest apiRequest, List<OffersPricingResponse> cosmosOffers) {

    // 1. Usa o mapper refatorado para construir a requisição complexa
    PricingRequest pricingRequest =
        pricingRequestMapper.buildFromCosmosOffers(apiRequest, cosmosOffers);

    // 2. Chama o serviço de pricing com o corpo da requisição correto
    List<InvestmentPricingConditionResponse> pricingData = pricingClient.getPrices(pricingRequest);

    if (pricingData == null || pricingData.isEmpty()) {
      log.warn("Serviço de pricing não retornou dados. As ofertas não serão enriquecidas.");
      return cosmosOffers; // Retorna as ofertas originais como fallback
    }

    // 3. Mescla os dados de pricing com as ofertas do cosmos
    log.info(
        "Mesclando {} ofertas do Cosmos com {} respostas do Pricing.",
        cosmosOffers.size(),
        pricingData.size());
    return pricingResponseMapper.mergeCosmosWithPricing(cosmosOffers, pricingData);
  }
}
