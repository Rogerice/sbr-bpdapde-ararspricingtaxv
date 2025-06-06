package com.santander.bp.model.external;

import feign.Contract;
import lombok.Data;

// InvestmentContract.java
@Data
public class InvestmentContract {
  private Contract contract;
  private Product product;
}
