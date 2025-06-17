package com.santander.bp.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

@Value
@Data
public class InvestmentOrder {

  @JsonProperty("investmentTradeChannel")
  private InvestmentTradeChannel investmentTradeChannel;

  @JsonProperty("netAmount")
  private NetAmount netAmount;

  @JsonProperty("investmentContract")
  private InvestmentContract investmentContract;

  @JsonProperty("typeCode")
  private String typeCode;

  @JsonProperty("promotionalCode")
  private PromotionalCode promotionalCode;

  @JsonProperty("audit")
  private Audit audit;
}
