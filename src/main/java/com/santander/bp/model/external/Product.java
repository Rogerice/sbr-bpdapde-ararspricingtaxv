package com.santander.bp.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Product {

  @JsonProperty("name")
  private String name;

  @JsonProperty("subproduct")
  private Subproduct subproduct;

  @JsonProperty("businessCategoryCode")
  private String businessCategoryCode;
}
