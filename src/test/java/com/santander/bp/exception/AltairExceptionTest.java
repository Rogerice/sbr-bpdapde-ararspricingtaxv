package com.santander.bp.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AltairExceptionTest {

  @Test
  public void testAltairException() {
    String code = "ERR001";
    String message = "An error occurred";
    String description = "Detailed error description";

    AltairException exception = new AltairException(code, message, description);

    assertEquals(code, exception.getCode());
    assertEquals(message, exception.getMessage());
    assertEquals(description, exception.getDescription());
  }
}
