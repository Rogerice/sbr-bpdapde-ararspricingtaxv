package com.santander.bp.service.whitelist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.santander.bp.entity.WhitelistEntity;
import com.santander.bp.model.WhitelistDTO;
import com.santander.bp.repository.WhitelistJpaRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WhitelistServiceTest {

  @Mock private WhitelistJpaRepository whitelistJpaRepository;

  @InjectMocks private WhitelistService whitelistService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // Tests for isInWhitelist
  @Test
  void testIsInWhitelist_whenDocumentExists() {
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of(new WhitelistEntity()));

    boolean result = whitelistService.isInWhitelist(documentType, documentNumber);

    assertTrue(result, "Expected true when document exists in whitelist");
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testIsInWhitelist_whenDocumentDoesNotExist() {
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of());

    boolean result = whitelistService.isInWhitelist(documentType, documentNumber);

    assertFalse(result, "Expected false when document does not exist in whitelist");
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testIsInWhitelist_whenRepositoryThrowsException() {
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenThrow(new RuntimeException("Database error"));

    boolean result = whitelistService.isInWhitelist(documentType, documentNumber);

    assertFalse(result, "Expected false when repository throws exception");
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testIsInWhitelist_withNullValues() {
    boolean result = whitelistService.isInWhitelist(null, null);

    assertFalse(result, "Expected false when documentType and documentNumber are null");
    verify(whitelistJpaRepository, times(1)).findByDocumentTypeAndDocumentNumber(null, null);
  }

  @Test
  void testIsAgencyInWhitelist_whenAgencyExists() {
    String agencia = "001";
    when(whitelistJpaRepository.findByAgencia(agencia)).thenReturn(List.of(new WhitelistEntity()));

    boolean result = whitelistService.isAgencyInWhitelist(agencia);

    assertTrue(result, "Expected true when agency exists in whitelist");
    verify(whitelistJpaRepository, times(1)).findByAgencia(agencia);
  }

  @Test
  void testIsAgencyInWhitelist_whenAgencyDoesNotExist() {
    String agencia = "001";
    when(whitelistJpaRepository.findByAgencia(agencia)).thenReturn(List.of());

    boolean result = whitelistService.isAgencyInWhitelist(agencia);

    assertFalse(result, "Expected false when agency does not exist in whitelist");
    verify(whitelistJpaRepository, times(1)).findByAgencia(agencia);
  }

  @Test
  void testIsAgencyInWhitelist_whenRepositoryThrowsException() {
    String agencia = "001";
    when(whitelistJpaRepository.findByAgencia(agencia))
        .thenThrow(new RuntimeException("Database error"));

    boolean result = whitelistService.isAgencyInWhitelist(agencia);

    assertFalse(result, "Expected false when repository throws exception");
    verify(whitelistJpaRepository, times(1)).findByAgencia(agencia);
  }

  @Test
  void testGetWhitelistDetails_whenDataExists() {
    String documentType = "CPF";
    String documentNumber = "12345678900";
    WhitelistEntity entity = new WhitelistEntity();
    entity.setId(1L);
    entity.setDocumentType(documentType);
    entity.setDocumentNumber(documentNumber);
    entity.setAgencia("001");

    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of(entity));

    List<WhitelistDTO> result = whitelistService.getWhitelistDetails(documentType, documentNumber);

    assertEquals(1, result.size(), "Expected one result when data exists");
    assertEquals(documentType, result.get(0).getDocumentType(), "Document type should match");
    assertEquals(documentNumber, result.get(0).getDocumentNumber(), "Document number should match");
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testGetWhitelistDetails_whenNoDataExists() {
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of());

    List<WhitelistDTO> result = whitelistService.getWhitelistDetails(documentType, documentNumber);

    assertTrue(result.isEmpty(), "Expected empty list when no data exists");
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testGetWhitelistDetails_withNullResponseFromRepository() {
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(null);

    List<WhitelistDTO> result = whitelistService.getWhitelistDetails(documentType, documentNumber);

    assertTrue(result.isEmpty(), "Expected empty list when repository returns null");
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }
}
