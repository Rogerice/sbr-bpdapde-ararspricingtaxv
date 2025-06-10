package com.santander.bp.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Data
public class WhitelistDTO {
  private Long id; // Ajustado para Long
  private String documentType;
  private String documentNumber;
  private String agencia;

  // Construtor personalizado (se necessário)
  public WhitelistDTO(Long id, String documentType, String documentNumber, String agencia) {
    this.id = id;
    this.documentType = documentType;
    this.documentNumber = documentNumber;
    this.agencia = agencia;
  }
}
