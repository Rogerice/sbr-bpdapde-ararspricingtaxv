package com.santander.bp.model.altair;

import com.altec.bsbr.fw.ps.annotations.PsFieldString;
import com.altec.bsbr.fw.ps.annotations.PsFormat;
import com.altec.bsbr.fw.ps.enums.PsAlign;

@PsFormat(name = "BPMP821")
public class BPMP821 {
  @PsFieldString(name = "INDREA", length = 1, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String INDREA;

  @PsFieldString(name = "PRODREA", length = 2, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String PRODREA;

  @PsFieldString(name = "SUBPREA", length = 4, paddingAlign = PsAlign.LEFT, paddingChar = ' ')
  private String SUBPREA;

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
