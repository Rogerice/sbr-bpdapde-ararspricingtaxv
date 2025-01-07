package com.santander.bp.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CosmosDbExceptionTest {

  @Test
  public void testCosmosDbException() {
    String code = "ERR002";
    String message = "Cosmos DB error occurred";
    String description = "Detailed Cosmos DB error description";

    CosmosDbException exception = new CosmosDbException(code, message, description);

    assertEquals(code, exception.getCode());
    assertEquals(message, exception.getMessage());
    assertEquals(description, exception.getDescription());
  }
}
