package com.santander.bp.model.altair;

import com.altec.bsbr.fw.ps.annotations.PsFieldNumber;
import com.altec.bsbr.fw.ps.annotations.PsFieldString;
import com.altec.bsbr.fw.ps.annotations.PsFormat;
import com.altec.bsbr.fw.ps.enums.PsAlign;
import lombok.Generated;

@PsFormat(name = "BPMP820")
@Generated
public class BPMP820 {
  @PsFieldString(name = "PRODUTO", length = 2, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String PRODUTO;

  @PsFieldString(name = "SUBPROD", length = 4, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String SUBPROD;

  @PsFieldString(name = "DESSUBP", length = 20, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String DESSUBP;

  @PsFieldString(name = "FAMILIA", length = 5, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String FAMILIA;

  @PsFieldString(name = "INDPRG", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String INDPRG;

  @PsFieldString(name = "OTRPRAZ", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String OTRPRAZ;

  @PsFieldString(name = "INAPLI", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String INAPLI;

  @PsFieldString(name = "INRESG", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String INRESG;

  @PsFieldString(name = "AGNFUTU", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String AGNFUTU;

  @PsFieldString(name = "AGNRESG", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String AGNRESG;

  @PsFieldString(name = "HRONLIN", length = 8, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String HRONLIN;

  @PsFieldNumber(name = "PRAZO1", length = 5, defaultValue = "0")
  private Integer PRAZO1;

  @PsFieldNumber(name = "PRAZO2", length = 5, defaultValue = "0")
  private Integer PRAZO2;

  @PsFieldNumber(name = "PRAZO3", length = 5, defaultValue = "0")
  private Integer PRAZO3;

  @PsFieldNumber(name = "PRZMIN", length = 5, defaultValue = "0")
  private Integer PRZMIN;

  @PsFieldNumber(name = "PRZMAX", length = 5, defaultValue = "0")
  private Integer PRZMAX;

  @PsFieldString(name = "INCARE", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String INCARE;

  @PsFieldNumber(name = "PRZCARE", length = 5, defaultValue = "0")
  private Integer PRZCARE;

  @PsFieldNumber(name = "VLRMINA", length = 15, decimal = 2, defaultValue = "0")
  private double VLRMINA;

  @PsFieldNumber(name = "VLRMINR", length = 15, decimal = 2, defaultValue = "0")
  private double VLRMINR;

  @PsFieldNumber(name = "SLDMIN", length = 15, decimal = 2, defaultValue = "0")
  private double SLDMIN;

  @PsFieldString(name = "MENSAG1", length = 85, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String MENSAG1;

  @PsFieldString(name = "MENSAG2", length = 85, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String MENSAG2;

  @PsFieldString(name = "MENSAG3", length = 85, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String MENSAG3;

  @PsFieldNumber(name = "TAXAENC", length = 8, decimal = 5, defaultValue = "0")
  private double TAXAENC;

  @PsFieldString(name = "DESTAXA", length = 25, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String DESTAXA;

  @PsFieldNumber(name = "TAXAREC", length = 8, decimal = 5, defaultValue = "0")
  private double TAXAREC;

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

  public String getDESSUBP() {
    return DESSUBP;
  }

  public void setDESSUBP(String DESSUBP) {
    this.DESSUBP = DESSUBP;
  }

  public String getFAMILIA() {
    return FAMILIA;
  }

  public void setFAMILIA(String FAMILIA) {
    this.FAMILIA = FAMILIA;
  }

  public String getINDPRG() {
    return INDPRG;
  }

  public void setINDPRG(String INDPRG) {
    this.INDPRG = INDPRG;
  }

  public String getOTRPRAZ() {
    return OTRPRAZ;
  }

  public void setOTRPRAZ(String OTRPRAZ) {
    this.OTRPRAZ = OTRPRAZ;
  }

  public String getINAPLI() {
    return INAPLI;
  }

  public void setINAPLI(String INAPLI) {
    this.INAPLI = INAPLI;
  }

  public String getINRESG() {
    return INRESG;
  }

  public void setINRESG(String INRESG) {
    this.INRESG = INRESG;
  }

  public String getAGNFUTU() {
    return AGNFUTU;
  }

  public void setAGNFUTU(String AGNFUTU) {
    this.AGNFUTU = AGNFUTU;
  }

  public String getAGNRESG() {
    return AGNRESG;
  }

  public void setAGNRESG(String AGNRESG) {
    this.AGNRESG = AGNRESG;
  }

  public String getHRONLIN() {
    return HRONLIN;
  }

  public void setHRONLIN(String HRONLIN) {
    this.HRONLIN = HRONLIN;
  }

  public Integer getPRAZO1() {
    return PRAZO1;
  }

  public void setPRAZO1(Integer pRAZO1) {
    PRAZO1 = pRAZO1;
  }

  public Integer getPRAZO2() {
    return PRAZO2;
  }

  public void setPRAZO2(Integer pRAZO2) {
    PRAZO2 = pRAZO2;
  }

  public Integer getPRAZO3() {
    return PRAZO3;
  }

  public void setPRAZO3(Integer pRAZO3) {
    PRAZO3 = pRAZO3;
  }

  public Integer getPRZMIN() {
    return PRZMIN;
  }

  public void setPRZMIN(Integer pRZMIN) {
    PRZMIN = pRZMIN;
  }

  public Integer getPRZMAX() {
    return PRZMAX;
  }

  public void setPRZMAX(Integer pRZMAX) {
    PRZMAX = pRZMAX;
  }

  public String getINCARE() {
    return INCARE;
  }

  public void setINCARE(String INCARE) {
    this.INCARE = INCARE;
  }

  public Integer getPRZCARE() {
    return PRZCARE;
  }

  public void setPRZCARE(Integer pRZCARE) {
    PRZCARE = pRZCARE;
  }

  public double getVLRMINA() {
    return VLRMINA;
  }

  public void setVLRMINA(double vLRMINA) {
    VLRMINA = vLRMINA;
  }

  public double getVLRMINR() {
    return VLRMINR;
  }

  public void setVLRMINR(double vLRMINR) {
    VLRMINR = vLRMINR;
  }

  public double getSLDMIN() {
    return SLDMIN;
  }

  public void setSLDMIN(double sLDMIN) {
    SLDMIN = sLDMIN;
  }

  public String getMENSAG1() {
    return MENSAG1;
  }

  public void setMENSAG1(String MENSAG1) {
    this.MENSAG1 = MENSAG1;
  }

  public String getMENSAG2() {
    return MENSAG2;
  }

  public void setMENSAG2(String MENSAG2) {
    this.MENSAG2 = MENSAG2;
  }

  public String getMENSAG3() {
    return MENSAG3;
  }

  public void setMENSAG3(String MENSAG3) {
    this.MENSAG3 = MENSAG3;
  }

  public double getTAXAENC() {
    return TAXAENC;
  }

  public void setTAXAENC(double tAXAENC) {
    TAXAENC = tAXAENC;
  }

  public String getDESTAXA() {
    return DESTAXA;
  }

  public void setDESTAXA(String DESTAXA) {
    this.DESTAXA = DESTAXA;
  }

  public double getTAXAREC() {
    return TAXAREC;
  }

  public void setTAXAREC(double tAXAREC) {
    TAXAREC = tAXAREC;
  }
}
