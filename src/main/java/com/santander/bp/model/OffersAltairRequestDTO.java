package com.santander.bp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class OffersAltairRequestDTO {
  // Fields to match BPMP82
  private String banco;
  private String canal;
  private String produto;
  private String subProd;
  private String familia;
  private String penumper;
  private String segment;
  private String tpfFunc;
  private String indRea;
  private String prodRea;
  private String subPrea;
}
