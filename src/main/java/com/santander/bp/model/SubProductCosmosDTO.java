package com.santander.bp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
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

  /** Construtor manual que recebe todos os campos e cria uma cópia defensiva da lista. */
  public SubProductCosmosDTO(
      String nmSubp,
      String cdSubp,
      Integer vlMiniApli,
      Integer vlMiniResg,
      Integer vlMiniSald,
      String inRemuPgre,
      String dsIndx,
      List<PrazoRemuneracaoProgressivaDTO> pzRemuPgre,
      String inCare) {
    this.nmSubp = nmSubp;
    this.cdSubp = cdSubp;
    this.vlMiniApli = vlMiniApli;
    this.vlMiniResg = vlMiniResg;
    this.vlMiniSald = vlMiniSald;
    this.inRemuPgre = inRemuPgre;
    this.dsIndx = dsIndx;
    // Chama o nosso setter seguro para criar a cópia
    this.setPzRemuPgre(pzRemuPgre);
    this.inCare = inCare;
  }

  /**
   * Setter manual para o campo da lista. Ele cria uma cópia defensiva para proteger a lista
   * interna.
   */
  public void setPzRemuPgre(List<PrazoRemuneracaoProgressivaDTO> pzRemuPgre) {
    if (pzRemuPgre == null) {
      this.pzRemuPgre = null;
    } else {
      this.pzRemuPgre = new ArrayList<>(pzRemuPgre);
    }
  }

  /**
   * Getter manual para o campo da lista. Retorna uma CÓPIA não modificável da lista para proteger o
   * estado interno.
   */
  public List<PrazoRemuneracaoProgressivaDTO> getPzRemuPgre() {
    if (this.pzRemuPgre == null) {
      return null;
    }
    return Collections.unmodifiableList(this.pzRemuPgre);
  }
}
