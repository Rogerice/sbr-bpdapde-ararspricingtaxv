package com.santander.bp.repository;

import com.santander.bp.model.WhitelistEntry;
import java.util.Optional;

public interface WhitelistRepository {
  Optional<WhitelistEntry> findByCpf(String cpf);

  Optional<WhitelistEntry> findByAgency(String agency);
}
