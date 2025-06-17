package com.santander.bp.model.external;

import lombok.Value;

@Value
public class InvestmentPricingCondition {
  private Product product;
  private BenchmarkIndex benchmarkIndex;
  private PromotionalCode promotionalCode;
}
