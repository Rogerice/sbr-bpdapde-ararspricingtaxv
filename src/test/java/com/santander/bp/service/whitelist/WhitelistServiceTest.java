package com.santander.bp.service.whitelist;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.santander.bp.model.WhitelistEntryDTO;
import com.santander.bp.repository.WhitelistRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WhitelistServiceTest {

  @Mock private WhitelistRepository whitelistRepository;

  @InjectMocks private WhitelistService whitelistService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIsInWhitelist_DocumentInWhitelist() {
    String documentType = "CPF";
    String documentNumber = "12345678909";

    WhitelistEntryDTO whitelistEntry =
        new WhitelistEntryDTO(documentType, documentNumber, "002", "Agência 002");

    when(whitelistRepository.findByDocument(documentType, documentNumber))
        .thenReturn(Optional.of(whitelistEntry));

    // Act
    boolean result = whitelistService.isInWhitelist(documentType, documentNumber);

    assertTrue(result);
  }

  @Test
  void testIsInWhitelist_DocumentNotInWhitelist() {

    String documentType = "CPF";
    String documentNumber = "98765432100";

    when(whitelistRepository.findByDocument(documentType, documentNumber))
        .thenReturn(Optional.empty());

    boolean result = whitelistService.isInWhitelist(documentType, documentNumber);

    assertFalse(result);
  }
}
