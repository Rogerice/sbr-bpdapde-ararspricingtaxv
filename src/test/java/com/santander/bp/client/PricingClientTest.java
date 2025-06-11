package com.santander.bp.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

/**
 * Testes unitários para a classe PricingClient. O objetivo é testar a lógica de comunicação com o
 * serviço de precificação, mockando o RestTemplate para isolar o teste de chamadas de rede reais.
 */
@ExtendWith(MockitoExtension.class)
class PricingClientTest {

  // Mock do RestTemplate, que é a dependência externa do nosso cliente.
  @Mock private RestTemplate restTemplate;

  // Injeta os mocks (neste caso, o restTemplate) na instância do PricingClient.
  @InjectMocks private PricingClient pricingClient;

  private PricingRequest pricingRequest;

  // A URL é definida aqui para garantir que os testes usem o mesmo endpoint que a
  // classe.
  private final String pricingUrl = "http://localhost:9999/prices";

  @BeforeEach
  void setUp() {
    // Cria um objeto de requisição padrão para ser usado em cada teste.
    pricingRequest = new PricingRequest();
  }

  @Test
  @DisplayName(
      "Deve retornar uma lista de condições de precificação quando a chamada for bem-sucedida")
  void getPrices_shouldReturnPricingConditions_whenCallIsSuccessful() {
    // Arrange (Organização)
    // 1. Prepara a resposta que esperamos do serviço mockado.
    InvestmentPricingConditionResponse condition = new InvestmentPricingConditionResponse();
    PricingResponse mockResponse = new PricingResponse();
    mockResponse.setInvestmentPricingConditions(Collections.singletonList(condition));

    // 2. Configura o mock: quando o método 'postForObject' for chamado com os
    // parâmetros esperados,
    // ele deve retornar a nossa resposta mockada.
    when(restTemplate.postForObject(
            eq(pricingUrl), any(PricingRequest.class), eq(PricingResponse.class)))
        .thenReturn(mockResponse);

    // Act (Ação)
    // 3. Executa o método que queremos testar.
    List<InvestmentPricingConditionResponse> result = pricingClient.getPrices(pricingRequest);

    // Assert (Verificação)
    // 4. Verifica se o resultado é o esperado.
    assertNotNull(result, "O resultado não deveria ser nulo.");
    assertEquals(1, result.size(), "A lista deveria conter um elemento.");
    assertEquals(condition, result.get(0), "O elemento da lista não é o esperado.");

    // 5. Verifica se o mock foi chamado corretamente.
    verify(restTemplate)
        .postForObject(eq(pricingUrl), any(PricingRequest.class), eq(PricingResponse.class));
  }

  @Test
  @DisplayName("Deve retornar uma lista vazia quando a resposta do serviço for nula")
  void getPrices_shouldReturnEmptyList_whenServiceResponseIsNull() {
    // Arrange
    // Configura o mock para retornar nulo, simulando uma resposta inesperada do
    // serviço.
    when(restTemplate.postForObject(
            eq(pricingUrl), any(PricingRequest.class), eq(PricingResponse.class)))
        .thenReturn(null);

    // Act
    List<InvestmentPricingConditionResponse> result = pricingClient.getPrices(pricingRequest);

    // Assert
    // Verifica se a nossa classe trata corretamente a resposta nula, retornando uma
    // lista vazia.
    assertNotNull(result, "O resultado não deveria ser nulo.");
    assertEquals(0, result.size(), "A lista deveria estar vazia quando a resposta é nula.");
  }

  @Test
  @DisplayName("Deve retornar uma lista vazia quando o RestTemplate lançar uma exceção")
  void getPrices_shouldReturnEmptyList_whenRestTemplateThrowsException() {
    // Arrange
    // Configura o mock para lançar uma exceção, simulando um erro de comunicação.
    when(restTemplate.postForObject(
            eq(pricingUrl), any(PricingRequest.class), eq(PricingResponse.class)))
        .thenThrow(new RestClientException("Erro de comunicação simulado"));

    // Act
    List<InvestmentPricingConditionResponse> result = pricingClient.getPrices(pricingRequest);

    // Assert
    // Verifica se o bloco try-catch da nossa classe funciona e retorna a lista
    // vazia de fallback.
    assertNotNull(result, "O resultado não deveria ser nulo, mesmo em caso de erro.");
    assertEquals(0, result.size(), "A lista deveria estar vazia quando ocorre uma exceção.");
  }
}
