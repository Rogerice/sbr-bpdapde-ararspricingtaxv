package com.santander.bp.service;

import com.santander.bp.app.mapper.PricingRequestMapper;
import com.santander.bp.app.mapper.PricingResponseMapper;
import com.santander.bp.client.PricingClient;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import com.santander.bp.model.external.PricingRequest;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;

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

  public List<OffersPricingResponse> getOffers(String segment, String channel, String product) {
    return cosmosDbService.getOffers(segment, channel, product);
  }

  public List<OffersPricingResponse> enrichOffersWithPricing(
      List<OffersPricingResponse> cosmosOffers) {
    PricingRequest pricingRequest = pricingRequestMapper.buildFromCosmosOffers(cosmosOffers);
    List<InvestmentPricingConditionResponse> pricingData = pricingClient.getPrices(pricingRequest);

    if (pricingData == null || pricingData.isEmpty()) {
      return cosmosOffers; // fallback
    }

    return pricingResponseMapper.mergeCosmosWithPricing(cosmosOffers, pricingData);
  }
}
