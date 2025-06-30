package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPricingConditionResponse {
  private Price price;
  private Product product;
  private BenchmarkIndex benchmarkIndex;
  private String reason;
  private Double fundingRate;
  private PromotionalCode promotionalCode;
}
