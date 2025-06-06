package com.santander.bp.config;

import lombok.Generated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Generated
public class CustomRestTemplateConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
