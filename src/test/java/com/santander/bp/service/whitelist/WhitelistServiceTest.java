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

  @Test
  void testIsInWhitelist_whenDocumentExists() {
    // Arrange
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of(new WhitelistEntity()));

    // Act
    boolean result = whitelistService.isInWhitelist(documentType, documentNumber);

    // Assert
    assertTrue(result);
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testIsInWhitelist_whenDocumentDoesNotExist() {
    // Arrange
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of());

    // Act
    boolean result = whitelistService.isInWhitelist(documentType, documentNumber);

    // Assert
    assertFalse(result);
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testIsAgencyInWhitelist_whenAgencyExists() {
    // Arrange
    String agencia = "001";
    when(whitelistJpaRepository.findByAgencia(agencia)).thenReturn(List.of(new WhitelistEntity()));

    // Act
    boolean result = whitelistService.isAgencyInWhitelist(agencia);

    // Assert
    assertTrue(result);
    verify(whitelistJpaRepository, times(1)).findByAgencia(agencia);
  }

  @Test
  void testIsAgencyInWhitelist_whenAgencyDoesNotExist() {
    // Arrange
    String agencia = "001";
    when(whitelistJpaRepository.findByAgencia(agencia)).thenReturn(List.of());

    // Act
    boolean result = whitelistService.isAgencyInWhitelist(agencia);

    // Assert
    assertFalse(result);
    verify(whitelistJpaRepository, times(1)).findByAgencia(agencia);
  }

  @Test
  void testGetWhitelistDetails_whenDataExists() {
    // Arrange
    String documentType = "CPF";
    String documentNumber = "12345678900";
    WhitelistEntity entity = new WhitelistEntity();
    entity.setId(1L);
    entity.setDocumentType(documentType);
    entity.setDocumentNumber(documentNumber);
    entity.setAgencia("001");

    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of(entity));

    // Act
    List<WhitelistDTO> result = whitelistService.getWhitelistDetails(documentType, documentNumber);

    // Assert
    assertEquals(1, result.size());
    assertEquals(documentType, result.get(0).getDocumentType());
    assertEquals(documentNumber, result.get(0).getDocumentNumber());
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testGetWhitelistDetails_whenNoDataExists() {
    // Arrange
    String documentType = "CPF";
    String documentNumber = "12345678900";
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber))
        .thenReturn(List.of());

    // Act
    List<WhitelistDTO> result = whitelistService.getWhitelistDetails(documentType, documentNumber);

    // Assert
    assertTrue(result.isEmpty());
    verify(whitelistJpaRepository, times(1))
        .findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
  }

  @Test
  void testGetWhitelistDetailsByAgency_whenDataExists() {
    // Arrange
    String agencia = "001";
    WhitelistEntity entity = new WhitelistEntity();
    entity.setId(1L);
    entity.setDocumentType("CPF");
    entity.setDocumentNumber("12345678900");
    entity.setAgencia(agencia);

    when(whitelistJpaRepository.findByAgencia(agencia)).thenReturn(List.of(entity));

    // Act
    List<WhitelistDTO> result = whitelistService.getWhitelistDetailsByAgency(agencia);

    // Assert
    assertEquals(1, result.size());
    assertEquals(agencia, result.get(0).getAgencia());
    verify(whitelistJpaRepository, times(1)).findByAgencia(agencia);
  }

  @Test
  void testGetWhitelistDetailsByAgency_whenNoDataExists() {
    // Arrange
    String agencia = "001";
    when(whitelistJpaRepository.findByAgencia(agencia)).thenReturn(List.of());

    // Act
    List<WhitelistDTO> result = whitelistService.getWhitelistDetailsByAgency(agencia);

    // Assert
    assertTrue(result.isEmpty());
    verify(whitelistJpaRepository, times(1)).findByAgencia(agencia);
  }
}
