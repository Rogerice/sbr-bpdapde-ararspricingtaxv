package com.santander.bp.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.santander.bp.model.external.InvestmentPricingConditionResponse;
import com.santander.bp.model.external.PricingRequest;
import com.santander.bp.model.external.PricingResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class PricingClientTest {

  @Mock(lenient = true)
  private RestTemplate restTemplate;

  @InjectMocks private PricingClient pricingClient;

  private PricingRequest pricingRequest;

  @BeforeEach
  void setUp() {
    pricingRequest = new PricingRequest();
  }

  @Test
  @DisplayName(
      "Deve retornar uma lista de condições de precificação quando a chamada for bem-sucedida")
  void getPrices_shouldReturnPricingConditions_whenCallIsSuccessful() {
    InvestmentPricingConditionResponse condition = new InvestmentPricingConditionResponse();
    PricingResponse mockResponse = new PricingResponse();
    mockResponse.setInvestmentPricingConditions(Collections.singletonList(condition));

    when(restTemplate.postForObject(
            anyString(), any(PricingRequest.class), eq(PricingResponse.class)))
        .thenReturn(mockResponse);

    List<InvestmentPricingConditionResponse> result = pricingClient.getPrices(pricingRequest);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(condition, result.get(0));

    verify(restTemplate)
        .postForObject(anyString(), any(PricingRequest.class), eq(PricingResponse.class));
  }

  @Test
  @DisplayName("Deve retornar uma lista vazia quando a resposta do serviço for nula")
  void getPrices_shouldReturnEmptyList_whenServiceResponseIsNull() {
    when(restTemplate.postForObject(
            anyString(), any(PricingRequest.class), eq(PricingResponse.class)))
        .thenReturn(null);

    List<InvestmentPricingConditionResponse> result = pricingClient.getPrices(pricingRequest);

    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  @DisplayName("Deve retornar uma lista vazia quando o RestTemplate lançar uma exceção")
  void getPrices_shouldReturnEmptyList_whenRestTemplateThrowsException() {
    when(restTemplate.postForObject(
            anyString(), any(PricingRequest.class), eq(PricingResponse.class)))
        .thenThrow(new RestClientException("Erro de comunicação simulado"));

    List<InvestmentPricingConditionResponse> result = pricingClient.getPrices(pricingRequest);

    assertNotNull(result);
    assertEquals(0, result.size());
  }
}
