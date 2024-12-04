package com.santander.bp.repository;

import com.santander.bp.model.WhitelistEntryDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.stereotype.Repository;

@Generated
@Repository
public class MockWhitelistRepository implements WhitelistRepository {

  private final Map<String, WhitelistEntryDTO> whitelistEntries;
  private final Map<String, WhitelistEntryDTO> agencyWhitelist;

  public MockWhitelistRepository() {
    this.whitelistEntries = new HashMap<>();
    this.agencyWhitelist = new HashMap<>();

    whitelistEntries.put(
        "CPF-12345678909", new WhitelistEntryDTO("CPF", "12345678909", "002", "Agência 002"));
    whitelistEntries.put(
        "CPF-98765432100", new WhitelistEntryDTO("CPF", "98765432100", "003", "Agência 003"));
    whitelistEntries.put(
        "CNPJ-12345678000195",
        new WhitelistEntryDTO("CNPJ", "12345678000195", "006", "Agência 006"));
    whitelistEntries.put(
        "CNPJ-98765432000110",
        new WhitelistEntryDTO("CNPJ", "98765432000110", "007", "Agência 007"));

    agencyWhitelist.put("002", new WhitelistEntryDTO("CPF", "12345678909", "002", "Agência 002"));
    agencyWhitelist.put("003", new WhitelistEntryDTO("CPF", "98765432100", "003", "Agência 003"));
    agencyWhitelist.put(
        "006", new WhitelistEntryDTO("CNPJ", "12345678000195", "006", "Agência 006"));
    agencyWhitelist.put(
        "007", new WhitelistEntryDTO("CNPJ", "98765432000110", "007", "Agência 007"));
  }

  @Override
  public Optional<WhitelistEntryDTO> findByDocument(String documentType, String documentNumber) {
    String key = documentType.toUpperCase() + "-" + documentNumber;
    return Optional.ofNullable(whitelistEntries.get(key));
  }

  @Override
  public Optional<WhitelistEntryDTO> findByAgency(String agency) {
    return Optional.ofNullable(agencyWhitelist.get(agency));
  }
}
