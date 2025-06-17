package com.santander.bp.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;

@Value
public class PricingRequest {
  @JsonProperty("investmentOrder")
  private InvestmentOrder investmentOrder;

  @JsonProperty("investmentPricingConditions")
  private List<InvestmentPricingCondition> investmentPricingConditions;
}
