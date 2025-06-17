package com.santander.bp.model.external;

import lombok.Value;

@Value
public class Tier {
  private Term term;
  private String aprPeriodInterest;
}
