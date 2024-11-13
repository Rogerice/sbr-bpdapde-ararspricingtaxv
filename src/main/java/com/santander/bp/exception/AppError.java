package com.santander.bp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppError {
  NOT_LIST_ERROR("113", HttpStatus.NOT_FOUND, "TESTE DE ERRO COMOSOS", "LIST ERRO COSMOS", null),

  ALTAIR_ERROR(
      "113",
      HttpStatus.INTERNAL_SERVER_ERROR,
      "Altair Communication Error",
      "An error occurred while communicating with Altair",
      null),

  NOT_WHITELISTED("113", HttpStatus.INTERNAL_SERVER_ERROR, "White LIST", "ERROR", null);

  private final String code;
  private final HttpStatus httpStatus;
  private final String title;
  private final String message;
  private final Object details;

  AppError(String code, HttpStatus httpStatus, String title, String message, Object details) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.title = title;
    this.message = message;
    this.details = details;
  }

  public String getCode() {
    return code;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getTitle() {
    return title;
  }

  public String getMessage() {
    return message;
  }

  public Object getDetails() {
    return details;
  }
}
