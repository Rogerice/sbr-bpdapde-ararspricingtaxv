package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

  private String participantId;
  private Bank bank;
  private Person person;
}
