package com.santander.bp.exception;

import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {

  private final AppError error;

  private final String message;

  public RestApiException(AppError error) {
    this.error = error;
    this.message = null;
  }

  public RestApiException(AppError error, Exception e) {
    super(e);
    this.error = error;
    this.message = e.getMessage();
  }

  public RestApiException(Exception e) {
    super(e);
    this.error = AppError.CANNOT_GET_MF;
    this.message = e.getMessage();
  }
}
