package com.santander.bp.config;

import java.util.Collections;
import javax.net.ssl.SSLContext;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração do RestTemplate específica para o perfil 'local'. Esta configuração desabilita a
 * validação de SSL E adiciona o header customizado.
 */
@Configuration
@Profile("local")
public class LocalSslDisablingRestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() throws Exception {
    SSLContext sslContext =
        new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();

    SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);

    PoolingHttpClientConnectionManager connectionManager =
        PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(sslSocketFactory)
            .build();

    CloseableHttpClient httpClient =
        HttpClients.custom().setConnectionManager(connectionManager).build();

    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);

    RestTemplate restTemplate = new RestTemplate(requestFactory);

    // CORREÇÃO: Adiciona o interceptador para incluir o header x-santander-client-id
    restTemplate.setInterceptors(
        Collections.singletonList(
            new RestTemplateHeaderModifierInterceptor("x-santander-client-id", "BP")));

    return restTemplate;
  }
}
