package com.santander.bp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import com.santander.bp.model.external.PricingRequest;
import com.santander.bp.model.external.PricingResponse;
import java.util.List;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Generated
public class PricingClient {

  private final RestTemplate restTemplate;
  private final String pricingUrl;
  private final ObjectMapper objectMapper;

  public PricingClient(
      RestTemplate restTemplate,
      @Value("${services.pricing.url}") String pricingUrl,
      ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.pricingUrl = pricingUrl;
    this.objectMapper = objectMapper;
  }

  public List<InvestmentPricingConditionResponse> getPrices(PricingRequest request) {
    try {
      log.info("Chamando serviço de Pricing no endpoint: {}", pricingUrl);

      try {
        log.debug(
            "Enviando requisição para o Pricing: {}", objectMapper.writeValueAsString(request));
      } catch (JsonProcessingException e) {
        log.warn("Não foi possível serializar a requisição para o log: {}", e.getMessage());
      }

      // MUDANÇA: Usamos postForEntity para capturar a resposta como texto bruto primeiro.
      ResponseEntity<String> rawResponse =
          restTemplate.postForEntity(pricingUrl, request, String.class);

      // ADIÇÃO: Logamos o JSON bruto que recebemos. É aqui que veremos a estrutura real da
      // resposta.
      log.info("RAW JSON Response from Pricing Service: {}", rawResponse.getBody());

      // Agora, tentamos converter o texto bruto para nossos objetos Java.
      PricingResponse response =
          objectMapper.readValue(rawResponse.getBody(), PricingResponse.class);

      log.debug("Resposta desserializada com sucesso do Pricing: {}", response);
      return response != null ? response.getInvestmentPricingConditions() : List.of();

    } catch (HttpStatusCodeException e) {
      String responseBody = e.getResponseBodyAsString();
      log.error(
          "Erro HTTP recebido do serviço de Pricing. Status: {}. Response Body: {}",
          e.getStatusCode(),
          responseBody,
          e);
      return List.of();

    } catch (RestClientException | JsonProcessingException e) {
      log.error(
          "Erro de conexão ou ao processar JSON ao chamar o serviço de Pricing: {}",
          e.getMessage(),
          e);
      return List.of();
    }
  }
}
