package com.santander.bp.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Subproduct {

  @JsonProperty("code")
  private String code;
}
