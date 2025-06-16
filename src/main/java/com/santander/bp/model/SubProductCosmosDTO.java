package com.santander.bp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubProductCosmosDTO {
  @JsonProperty("NM_SUBP")
  private String nmSubp;

  @JsonProperty("CD_SUBP")
  private String cdSubp;

  @JsonProperty("VL_MINI_APLI")
  private Integer vlMiniApli;

  @JsonProperty("VL_MINI_RESG")
  private Integer vlMiniResg;

  @JsonProperty("VL_MINI_SALD")
  private Integer vlMiniSald;

  @JsonProperty("IN_REMU_PGRE")
  private String inRemuPgre;

  @JsonProperty("DS_INDX")
  private String dsIndx;

  @JsonProperty("PZ_REMU_PGRE")
  private List<PrazoRemuneracaoProgressivaDTO> pzRemuPgre;

  @JsonProperty("IN_CARE")
  private String inCare;
}
