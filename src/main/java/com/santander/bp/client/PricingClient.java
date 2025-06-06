package com.santander.bp.client;

import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import com.santander.bp.model.external.PricingRequest;
import com.santander.bp.model.external.PricingResponse;
import java.util.List;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Generated
public class PricingClient {

  private final RestTemplate restTemplate;

  // 2. Remova a URL "hardcoded"
  private static final String pricingUrl = "http://localhost:9999/prices";

  // 3. Crie um campo para injetar a URL do arquivo de configuração
  // @Value("${pricing.service.url}")
  // private String pricingUrl;

  public PricingClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<InvestmentPricingConditionResponse> getPrices(PricingRequest request) {
    try {
      log.info(
          "Chamando serviço de Pricing no endpoint: {}", pricingUrl); // Log para confirmar a URL
      PricingResponse response =
          restTemplate.postForObject(
              pricingUrl, request, PricingResponse.class); // 4. Use a nova variável
      return response != null ? response.getInvestmentPricingConditions() : List.of();
    } catch (Exception e) {
      log.warn("Erro ao chamar o serviço de Pricing: {}", e.getMessage());
      return List.of(); // fallback
    }
  }
}
