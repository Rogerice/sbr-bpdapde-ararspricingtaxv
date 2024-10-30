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
public class OffersAltairResponse {

  // Fields to match BPMP820
  private String produto;
  private String subProd;
  private String desSubp;
  private String familia;
  private String indPrg;
  private String otrPraz;
  private String inApli;
  private String inResg;
  private String agnFutu;
  private String agnResg;
  private String hrOnlin;
  private Integer prazo1;
  private Integer prazo2;
  private Integer prazo3;
  private double vlrMinA;
  private double taxaEnc;
}
