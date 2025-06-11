package com.santander.bp.client;

import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import com.santander.bp.model.external.PricingRequest;
import com.santander.bp.model.external.PricingResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PricingClient {

  private static final String PRICING_URL = "http://localhost:9999/prices";
  private final RestTemplate restTemplate;

  public PricingClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<InvestmentPricingConditionResponse> getPrices(PricingRequest request) {
    try {
      log.info("Chamando serviço de Pricing no endpoint: {}", PRICING_URL);
      PricingResponse response =
          restTemplate.postForObject(PRICING_URL, request, PricingResponse.class);
      return response != null ? response.getInvestmentPricingConditions() : List.of();
    } catch (Exception e) {
      log.warn("Erro ao chamar o serviço de Pricing: {}", e.getMessage());
      return List.of();
    }
  }
}
