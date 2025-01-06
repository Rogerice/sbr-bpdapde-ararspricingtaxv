package com.santander.bp.exception;

import lombok.Generated;
import org.springframework.http.HttpStatus;

@Generated
public class RestApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private final HttpStatus httpStatus;
  private final String code;
  private final String title;
  private final String message;
  private final transient Object details;

  public RestApiException(AppError appError) {
    super(appError.getMessage());
    this.httpStatus = appError.getHttpStatus();
    this.code = appError.getCode();
    this.title = appError.getTitle();
    this.message = appError.getMessage();
    this.details = appError.getDetails();
  }

  public RestApiException(
      HttpStatus httpStatus, String code, String title, String message, Object details) {
    super(message);
    this.httpStatus = httpStatus;
    this.code = code;
    this.title = title;
    this.message = message;
    this.details = details;
  }

  public RestApiException(String code, String message, String description) {
    super(message);
    this.httpStatus = HttpStatus.BAD_REQUEST;
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
