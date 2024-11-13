package com.santander.bp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhitelistEntryDTO {
  private String documentType; // CPF ou CNPJ
  private String documentNumber;
  private String agencyCode;
  private String agencyName;
}
