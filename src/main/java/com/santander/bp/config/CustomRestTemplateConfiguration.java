package com.santander.bp.config;

import java.util.Collections;
import lombok.Generated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/** Configuração do RestTemplate para ambientes de produção e pré-produção. */
@Configuration
@Profile("!local") // Ativo em todos os perfis, EXCETO 'local'
@Generated
public class CustomRestTemplateConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();

    // CORREÇÃO: Adiciona o interceptador para incluir o header x-santander-client-id
    // O valor "BP" foi tirado da sua imagem do Postman.
    restTemplate.setInterceptors(
        Collections.singletonList(
            new RestTemplateHeaderModifierInterceptor("x-santander-client-id", "BP")));

    return restTemplate;
  }
}
