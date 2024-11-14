package com.santander.bp.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AppCosmosDbConfigurationTest {

  @InjectMocks private AppCosmosDbConfiguration appCosmosDbConfiguration;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(appCosmosDbConfiguration, "uri", "http://cosmos-uri");
    ReflectionTestUtils.setField(appCosmosDbConfiguration, "key", "test-key");
    ReflectionTestUtils.setField(appCosmosDbConfiguration, "database", "test-database");
  }

  @Test
  void testCosmosClientBuilder() {
    CosmosClientBuilder cosmosClientBuilder = appCosmosDbConfiguration.cosmosClientBuilder();
    assertNotNull(cosmosClientBuilder, "CosmosClientBuilder should not be null");
  }

  @Test
  void testGetDatabaseName() {
    String databaseName = appCosmosDbConfiguration.getDatabaseName();
    assertNotNull(databaseName, "Database name should not be null");
  }

  @Test
  void testCosmosConfig() {
    CosmosConfig cosmosConfig = appCosmosDbConfiguration.cosmosConfig();
    assertNotNull(cosmosConfig, "CosmosConfig should not be null");
  }
}
