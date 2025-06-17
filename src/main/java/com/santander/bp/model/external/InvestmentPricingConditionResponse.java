package com.santander.bp.model.external;

import lombok.Generated;
import lombok.Value;

@Value
@Generated
public class InvestmentPricingConditionResponse {
  private Price price;
  private Product product;
  private BenchmarkIndex benchmarkIndex;
  private String reason;
  private Double fundingRate;
  private PromotionalCode promotionalCode;
}
