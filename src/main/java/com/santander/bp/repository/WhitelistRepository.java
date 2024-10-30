package com.santander.bp.repository;

import com.santander.bp.model.Whitelist;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class WhitelistRepository {

  private final List<Whitelist> whitelistEntries = new ArrayList<>();

  public WhitelistRepository() {
    whitelistEntries.add(new Whitelist("12345678901", "001"));
    whitelistEntries.add(new Whitelist("98765432100", "002"));
  }

  public Optional<Whitelist> findByCpfCnpj(String cpfCnpj) {
    return whitelistEntries.stream()
        .filter(entry -> entry.getCpfCnpj().equals(cpfCnpj))
        .findFirst();
  }

  public Optional<Whitelist> findByAgencia(String agencia) {
    return whitelistEntries.stream()
        .filter(entry -> entry.getAgencia().equals(agencia))
        .findFirst();
  }
}
