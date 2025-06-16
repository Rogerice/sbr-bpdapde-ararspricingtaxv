package com.santander.bp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrazoRemuneracaoProgressivaDTO {

  @JsonProperty("PZ_INIC_REMU_RESG")
  private Integer prazoInicial;

  @JsonProperty("PZ_FINA_REMU_RESG")
  private Integer prazoFinal;
}
