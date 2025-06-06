package com.santander.bp.model.external;

import lombok.Data;

// Participant.java
@Data
public class Participant {
  private String participantId;
  private Bank bank;
  private Person person;
}
