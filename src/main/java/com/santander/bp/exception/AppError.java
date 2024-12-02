package com.santander.bp.exception;

import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Generated
@Getter
public enum AppError {
  NOT_FOUND("171", HttpStatus.NOT_FOUND, "Cosmos", "Erro lista de erro retorno Cosmos", null),

  ALTAIR_ERROR(
      "157",
      HttpStatus.INTERNAL_SERVER_ERROR,
      "Erro de comunicação Altair",
      "Ocorreu um erro durante a comunicação com Altair",
      null),

  NOT_WHITELISTED("180", HttpStatus.INTERNAL_SERVER_ERROR, "White LIST", "ERROR", null);

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
