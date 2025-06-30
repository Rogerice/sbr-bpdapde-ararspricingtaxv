package com.santander.bp.config;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Interceptador para adicionar headers customizados em todas as requisições feitas por um
 * RestTemplate.
 */
public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

  private final String headerName;
  private final String headerValue;

  public RestTemplateHeaderModifierInterceptor(String headerName, String headerValue) {
    this.headerName = headerName;
    this.headerValue = headerValue;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    // Adiciona o header customizado na requisição
    request.getHeaders().add(this.headerName, this.headerValue);
    // Continua com a execução da requisição
    return execution.execute(request, body);
  }
}
