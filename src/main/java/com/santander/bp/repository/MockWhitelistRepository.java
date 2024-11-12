package com.santander.bp.repository;

import com.santander.bp.model.WhitelistEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MockWhitelistRepository implements WhitelistRepository {

  private final Map<String, WhitelistEntry> cpfWhitelist;
  private final Map<String, WhitelistEntry> agencyWhitelist;

  public MockWhitelistRepository() {
    this.cpfWhitelist = new HashMap<>();
    this.agencyWhitelist = new HashMap<>();

    cpfWhitelist.put("12345678900", new WhitelistEntry("12345678900", "001", "Agência 001"));
    agencyWhitelist.put("001", new WhitelistEntry("12345678900", "001", "Agência 001"));
  }

  @Override
  public Optional<WhitelistEntry> findByCpf(String cpf) {
    return Optional.ofNullable(cpfWhitelist.get(cpf));
  }

  @Override
  public Optional<WhitelistEntry> findByAgency(String agency) {
    return Optional.ofNullable(agencyWhitelist.get(agency));
  }
}
