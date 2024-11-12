package com.santander.bp.exception;

public class CosmosDbException extends RuntimeException {
  private final String code;
  private final String description;

  public CosmosDbException(String code, String message, String description) {
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
