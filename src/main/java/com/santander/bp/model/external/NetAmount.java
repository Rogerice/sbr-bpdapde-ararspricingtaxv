package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// NetAmount.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetAmount {
  private Double amount;
  private String currency;
}
