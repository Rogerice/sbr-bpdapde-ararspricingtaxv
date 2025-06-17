package com.santander.bp.model.external;

import feign.Contract;
import lombok.Value;

@Value
public class InvestmentContract {
  private Contract contract;
  private Product product;
}
