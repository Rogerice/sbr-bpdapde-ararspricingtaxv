package com.santander.bp.service;

import com.santander.bp.model.OffersPricingResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OffersService {

  private final CosmosDbService cosmosDbService;

  public OffersService(CosmosDbService cosmosDbService) {
    this.cosmosDbService = cosmosDbService;
  }

  @CircuitBreaker(name = "offersService", fallbackMethod = "fallbackGetOffers")
  public List<OffersPricingResponse> getOffers(String segment, String channel, String product) {
    return cosmosDbService.getOffers(segment, channel, product);
  }

  public List<OffersPricingResponse> fallbackGetOffers(
      String segment, String channel, String product, Throwable throwable) {
    return Collections.emptyList();
  }
}
