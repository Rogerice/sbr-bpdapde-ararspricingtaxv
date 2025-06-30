package com.santander.bp.model.external;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingResponse {
  private String investmentPriceProposalId;
  private List<InvestmentPricingConditionResponse> investmentPricingConditions;
}
