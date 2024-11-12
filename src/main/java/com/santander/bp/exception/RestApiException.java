package com.santander.bp.exception;

import org.springframework.http.HttpStatus;

public class RestApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private final HttpStatus httpStatus;
  private final int code;
  private final String title;
  private final String message;
  private final Object details;

  public RestApiException(AppError appError) {
    super(appError.getMessage());
    this.httpStatus = appError.getHttpStatus();
    this.code = appError.getCode();
    this.title = appError.getTitle();
    this.message = appError.getMessage();
    this.details = appError.getDetails();
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public int getCode() {
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
