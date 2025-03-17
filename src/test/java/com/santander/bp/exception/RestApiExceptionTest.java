package com.santander.bp.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class RestApiExceptionTest {

  @Test
  public void testRestApiExceptionWithAllParams() {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String code = "500";
    String title = "Internal Server Error";
    String message = "An unexpected error occurred";
    Object details = "Error details";

    RestApiException exception = new RestApiException(httpStatus, code, title, message, details);

    assertEquals(httpStatus, exception.getHttpStatus());
    assertEquals(code, exception.getCode());
    assertEquals(title, exception.getTitle());
    assertEquals(message, exception.getMessage());
    assertEquals(details, exception.getDetails());
  }

  @Test
  public void testRestApiExceptionWithCodeMessageDescription() {
    String code = "400";
    String message = "Bad Request";
    String description = "Invalid input";

    RestApiException exception = new RestApiException(code, message, description);

    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(code, exception.getCode());
    assertEquals("Erro ao comunicar com Altair", exception.getTitle());
    assertEquals(message, exception.getMessage());
    assertEquals(description, exception.getDetails());
  }

  @Test
  public void testSetDetails() {
    RestApiException exception = new RestApiException("400", "Bad Request", "Invalid input");
    Object newDetails = "New error details";
    exception.setDetails(newDetails);

    assertEquals(newDetails, exception.getDetails());
  }
}
