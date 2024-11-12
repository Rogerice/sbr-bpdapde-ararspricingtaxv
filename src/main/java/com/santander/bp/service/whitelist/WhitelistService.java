package com.santander.bp.service.whitelist;

import com.santander.bp.repository.WhitelistRepository;
import org.springframework.stereotype.Service;

@Service
public class WhitelistService {

  private final WhitelistRepository whitelistRepository;

  public WhitelistService(WhitelistRepository whitelistRepository) {
    this.whitelistRepository = whitelistRepository;
  }

  public boolean isInWhitelist(String documentNumber, String centerId) {
    if (whitelistRepository.findByCpf(documentNumber).isPresent()) {
      return true;
    }
    return whitelistRepository.findByAgency(centerId).isPresent();
  }
}
