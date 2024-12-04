package com.santander.bp.exception;

import lombok.Generated;

@Generated
public class AltairException extends RuntimeException {
  private final String code;
  private final String description;

  public AltairException(String code, String message, String description) {
    super(message);
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
