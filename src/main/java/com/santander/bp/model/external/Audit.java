package com.santander.bp.model.external;

import lombok.Generated;

@Generated
public class Audit {
  private String user;

  public Audit() {}

  public Audit(Audit other) {
    if (other != null) {
      this.user = other.user;
    }
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }
}
