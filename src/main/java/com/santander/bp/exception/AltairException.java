package com.santander.bp.exception;

import lombok.Generated;

@Generated
public class AltairException extends RuntimeException {
  /** */
  private static final long serialVersionUID = 1L;

  private final String code;
  private final String description;

  public AltairException(String code, String message, String description) {
    super(message);
    this.code = code;
    this.description = description;
  }

  public AltairException(String code, String message, String description, Throwable cause) {
    super(message, cause);
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
