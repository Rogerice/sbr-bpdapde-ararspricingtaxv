package com.santander.bp.model.altair;

import com.altec.bsbr.fw.ps.annotations.PsFieldString;
import com.altec.bsbr.fw.ps.annotations.PsFormat;
import com.altec.bsbr.fw.ps.enums.PsAlign;

@PsFormat(name = "BPMP82")
public class BPMP82 {
  @PsFieldString(name = "BANCO", length = 4, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String BANCO;

  @PsFieldString(name = "CANAL", length = 2, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String CANAL;

  @PsFieldString(name = "PRODUTO", length = 2, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String PRODUTO;

  @PsFieldString(name = "SUBPROD", length = 4, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String SUBPROD;

  @PsFieldString(name = "FAMILIA", length = 5, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String FAMILIA;

  @PsFieldString(name = "PENUMPE", length = 8, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String PENUMPE;

  @PsFieldString(name = "SEGMENT", length = 3, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String SEGMENT;

  @PsFieldString(name = "TPFUNC", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String TPFUNC;

  @PsFieldString(name = "INDREA", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String INDREA;

  @PsFieldString(name = "PRODREA", length = 2, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String PRODREA;

  @PsFieldString(name = "SUBPREA", length = 4, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String SUBPREA;

  public String getBANCO() {
    return BANCO;
  }

  public void setBANCO(String BANCO) {
    this.BANCO = BANCO;
  }

  public String getCANAL() {
    return CANAL;
  }

  public void setCANAL(String CANAL) {
    this.CANAL = CANAL;
  }

  public String getPRODUTO() {
    return PRODUTO;
  }

  public void setPRODUTO(String PRODUTO) {
    this.PRODUTO = PRODUTO;
  }

  public String getSUBPROD() {
    return SUBPROD;
  }

  public void setSUBPROD(String SUBPROD) {
    this.SUBPROD = SUBPROD;
  }

  public String getFAMILIA() {
    return FAMILIA;
  }

  public void setFAMILIA(String FAMILIA) {
    this.FAMILIA = FAMILIA;
  }

  public String getPENUMPE() {
    return PENUMPE;
  }

  public void setPENUMPE(String PENUMPE) {
    this.PENUMPE = PENUMPE;
  }

  public String getSEGMENT() {
    return SEGMENT;
  }

  public void setSEGMENT(String SEGMENT) {
    this.SEGMENT = SEGMENT;
  }

  public String getTPFUNC() {
    return TPFUNC;
  }

  public void setTPFUNC(String TPFUNC) {
    this.TPFUNC = TPFUNC;
  }

  public String getINDREA() {
    return INDREA;
  }

  public void setINDREA(String INDREA) {
    this.INDREA = INDREA;
  }

  public String getPRODREA() {
    return PRODREA;
  }

  public void setPRODREA(String PRODREA) {
    this.PRODREA = PRODREA;
  }

  public String getSUBPREA() {
    return SUBPREA;
  }

  public void setSUBPREA(String SUBPREA) {
    this.SUBPREA = SUBPREA;
  }
}
