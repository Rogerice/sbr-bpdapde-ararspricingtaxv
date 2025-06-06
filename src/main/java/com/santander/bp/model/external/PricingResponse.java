package com.santander.bp.model.external;

import java.util.List;
import lombok.Data;

@Data
public class PricingResponse {
  private String investmentPriceProposalId;
  private List<InvestmentPricingConditionResponse> investmentPricingConditions;
}
