package com.santander.bp.model.external;

import lombok.Value;

@Value
public class Participant {

  private String participantId;
  private Bank bank;
  private Person person;
}
