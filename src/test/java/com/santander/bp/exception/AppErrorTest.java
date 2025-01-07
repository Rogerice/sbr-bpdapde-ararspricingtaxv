package com.santander.bp.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AppErrorTest {

  @Test
  public void testNotFoundError() {
    AppError error = AppError.NOT_FOUND;

    assertEquals("400", error.getCode());
    assertEquals(HttpStatus.NOT_FOUND, error.getHttpStatus());
    assertEquals("Cosmos", error.getTitle());
    assertEquals("Erro lista de erro retorno Cosmos", error.getMessage());
    assertNull(error.getDetails());
  }

  @Test
  public void testAltairError() {
    AppError error = AppError.ALTAIR_ERROR;

    assertEquals("400", error.getCode());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getHttpStatus());
    assertEquals("Erro de comunicação Altair", error.getTitle());
    assertEquals("Ocorreu um erro durante a comunicação com Altair", error.getMessage());
    assertNull(error.getDetails());
  }

  @Test
  public void testNotWhitelistedError() {
    AppError error = AppError.NOT_WHITELISTED;

    assertEquals("400", error.getCode());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getHttpStatus());
    assertEquals("White LIST", error.getTitle());
    assertEquals("ERROR", error.getMessage());
    assertNull(error.getDetails());
  }
}
