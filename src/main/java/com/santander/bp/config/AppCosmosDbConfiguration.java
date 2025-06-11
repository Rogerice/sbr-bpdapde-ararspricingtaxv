package com.santander.bp.config;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Generated
@Configuration
@EnableCosmosRepositories(basePackages = "com.santander.bp.repository")
public class AppCosmosDbConfiguration extends AbstractCosmosConfiguration {

  @Value("${azure.cosmos.uri}")
  private String uri;

  @Value("${azure.cosmos.key}")
  private String key;

  @Value("${azure.cosmos.database}")
  private String database;

  @Bean
  public CosmosClientBuilder cosmosClientBuilder() {
    return new CosmosClientBuilder()
        .endpoint(uri)
        .key(key)
        .consistencyLevel(ConsistencyLevel.EVENTUAL);
  }

  @Override
  protected String getDatabaseName() {
    return database;
  }
}
