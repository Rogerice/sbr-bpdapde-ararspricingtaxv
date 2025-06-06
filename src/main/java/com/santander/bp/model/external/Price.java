package com.santander.bp.model.external;

import java.util.List;
import lombok.Data;

@Data
public class Price {
  private String code;
  private String priceConceptId;
  private List<Tier> tiers;
}
