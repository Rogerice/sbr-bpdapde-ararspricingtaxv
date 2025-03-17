package com.santander.bp.service;

import com.santander.bp.model.OffersPricingResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OffersService {

  private final CosmosDbService cosmosDbService;
  private final PricingEngineService pricingEngineService;

  public OffersService(CosmosDbService cosmosDbService, PricingEngineService pricingEngineService) {
    this.cosmosDbService = cosmosDbService;
    this.pricingEngineService = pricingEngineService;
  }

  public List<OffersPricingResponse> getOffers(String segment, String channel, String product) {
    List<OffersPricingResponse> offers = cosmosDbService.getOffers(segment, channel, product);

    return pricingEngineService.enrichOffersWithMockRates(offers);
  }

  public List<OffersPricingResponse> enrichOffersWithPricing(List<OffersPricingResponse> offers) {
    return pricingEngineService.enrichOffersWithMockRates(offers);
  }
}
