package com.santander.bp.service.whitelist;

import com.santander.bp.repository.WhitelistRepository;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Generated
@Service
public class WhitelistService {

  private final WhitelistRepository whitelistRepository;

  public WhitelistService(WhitelistRepository whitelistRepository) {
    this.whitelistRepository = whitelistRepository;
  }

  public boolean isInWhitelist(String documentType, String documentNumber) {
    return whitelistRepository.findByDocument(documentType, documentNumber).isPresent();
  }

  public boolean isAgencyInWhitelist(String centerId) {
    return whitelistRepository.findByAgency(centerId).isPresent();
  }
}
