package com.santander.bp.enums;

public enum FixedFieldsEnum {
  BANCO("0033"),
  PRODUTO("26");

  private final String value;

  FixedFieldsEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
