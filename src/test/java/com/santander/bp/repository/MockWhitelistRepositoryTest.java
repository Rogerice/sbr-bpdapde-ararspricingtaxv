package com.santander.bp.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.santander.bp.model.WhitelistEntryDTO;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockWhitelistRepositoryTest {

  private MockWhitelistRepository mockWhitelistRepository;

  @BeforeEach
  void setUp() {
    mockWhitelistRepository = new MockWhitelistRepository();
  }

  @Test
  void testFindByDocument_DocumentInWhitelist() {
    String documentType = "CPF";
    String documentNumber = "12345678909";

    Optional<WhitelistEntryDTO> result =
        mockWhitelistRepository.findByDocument(documentType, documentNumber);

    assertTrue(result.isPresent(), "O documento deveria estar presente na whitelist");
  }

  @Test
  void testFindByDocument_DocumentNotInWhitelist() {
    String documentType = "CPF";
    String documentNumber = "00000000000";

    Optional<WhitelistEntryDTO> result =
        mockWhitelistRepository.findByDocument(documentType, documentNumber);
    assertFalse(result.isPresent(), "O documento não deveria estar presente na whitelist");
  }

  @Test
  void testFindByAgency_AgencyInWhitelist() {
    String agency = "002";

    Optional<WhitelistEntryDTO> result = mockWhitelistRepository.findByAgency(agency);

    assertTrue(result.isPresent(), "A agência deveria estar presente na whitelist");
  }

  @Test
  void testFindByAgency_AgencyNotInWhitelist() {

    String agency = "999";

    Optional<WhitelistEntryDTO> result = mockWhitelistRepository.findByAgency(agency);

    assertFalse(result.isPresent(), "A agência não deveria estar presente na whitelist");
  }
}
