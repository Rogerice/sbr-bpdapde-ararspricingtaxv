package com.santander.bp.exception;

import org.springframework.http.HttpStatus;

public class RestApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private final HttpStatus httpStatus;
  private final String code;
  private final String title;
  private final String message;
  private final Object details;

  // Construtor padrão utilizando AppError
  public RestApiException(AppError appError) {
    super(appError.getMessage());
    this.httpStatus = appError.getHttpStatus();
    this.code = appError.getCode();
    this.title = appError.getTitle();
    this.message = appError.getMessage();
    this.details = appError.getDetails();
  }

  // Novo construtor para permitir informações customizadas
  public RestApiException(
      HttpStatus httpStatus, String code, String title, String message, Object details) {
    super(message);
    this.httpStatus = httpStatus;
    this.code = code;
    this.title = title;
    this.message = message;
    this.details = details;
  }

  // Construtor para ser usado especificamente com mensagem de erro do Altair
  public RestApiException(String code, String message, String description) {
    super(message);
    this.httpStatus =
        HttpStatus.BAD_REQUEST; // Default para BAD_REQUEST (pode ser ajustado se necessário)
    this.code = code;
    this.title = "Erro ao comunicar com Altair";
    this.message = message;
    this.details = description;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getCode() {
    return code;
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
