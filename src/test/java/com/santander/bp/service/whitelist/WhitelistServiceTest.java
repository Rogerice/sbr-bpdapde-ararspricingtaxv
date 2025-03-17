package com.santander.bp.service.whitelist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.santander.bp.entity.WhitelistEntity;
import com.santander.bp.model.WhitelistDTO;
import com.santander.bp.repository.WhitelistJpaRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WhitelistServiceTest {

  @Mock private WhitelistJpaRepository whitelistJpaRepository;

  @InjectMocks private WhitelistService whitelistService;

  private WhitelistEntity mockEntity;

  @BeforeEach
  void setUp() {
    mockEntity = new WhitelistEntity();
    mockEntity.setId(1L);
    mockEntity.setDocumentType("CPF");
    mockEntity.setDocumentNumber("12345678901");
    mockEntity.setAgencia("0001");
  }

  @Test
  void testIsInWhitelist_True() {
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber("CPF", "12345678901"))
        .thenReturn(List.of(mockEntity));

    boolean result = whitelistService.isInWhitelist("CPF", "12345678901");
    assertTrue(result);
  }

  @Test
  void testIsInWhitelist_False() {
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber("CPF", "12345678901"))
        .thenReturn(List.of());

    boolean result = whitelistService.isInWhitelist("CPF", "12345678901");
    assertFalse(result);
  }

  @Test
  void testIsAgencyInWhitelist_True() {
    when(whitelistJpaRepository.findByAgencia("0001")).thenReturn(List.of(mockEntity));
    boolean result = whitelistService.isAgencyInWhitelist("0001");
    assertTrue(result);
  }

  @Test
  void testIsAgencyInWhitelist_False() {
    when(whitelistJpaRepository.findByAgencia("0001")).thenReturn(List.of());
    boolean result = whitelistService.isAgencyInWhitelist("0001");
    assertFalse(result);
  }

  @Test
  void testGetWhitelistDetails_Success() {
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber("CPF", "12345678901"))
        .thenReturn(List.of(mockEntity));

    List<WhitelistDTO> result = whitelistService.getWhitelistDetails("CPF", "12345678901");
    assertEquals(1, result.size());
    assertEquals(mockEntity.getDocumentType(), result.get(0).getDocumentType());
  }

  @Test
  void testGetWhitelistDetailsByAgency_Success() {
    when(whitelistJpaRepository.findByAgencia("0001")).thenReturn(List.of(mockEntity));

    List<WhitelistDTO> result = whitelistService.getWhitelistDetailsByAgency("0001");
    assertEquals(1, result.size());
    assertEquals(mockEntity.getAgencia(), result.get(0).getAgencia());
  }

  @Test
  void testIsInWhitelist_ExceptionHandling() {
    when(whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(any(), any()))
        .thenThrow(new RuntimeException("Erro no banco de dados"));

    boolean result = whitelistService.isInWhitelist("CPF", "12345678901");
    assertFalse(result);
  }

  @Test
  void testIsAgencyInWhitelist_ExceptionHandling() {
    when(whitelistJpaRepository.findByAgencia(any()))
        .thenThrow(new RuntimeException("Erro no banco de dados"));

    boolean result = whitelistService.isAgencyInWhitelist("0001");
    assertFalse(result);
  }
}
