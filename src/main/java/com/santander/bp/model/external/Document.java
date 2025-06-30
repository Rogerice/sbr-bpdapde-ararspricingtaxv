package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Document.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
  private String documentNumber;
  private String documentTypeCode;
}
