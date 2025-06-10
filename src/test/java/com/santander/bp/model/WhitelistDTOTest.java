package com.santander.bp.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WhitelistDTOTest {

  @Test
  void testAllArgsConstructorAndGetters() {
    WhitelistDTO dto = new WhitelistDTO(1L, "CPF", "12345678901", "1234");

    assertEquals(1L, dto.getId());
    assertEquals("CPF", dto.getDocumentType());
    assertEquals("12345678901", dto.getDocumentNumber());
    assertEquals("1234", dto.getAgencia());
  }

  @Test
  void testNoArgsConstructorAndSetters() {
    WhitelistDTO dto = new WhitelistDTO();
    dto.setId(2L);
    dto.setDocumentType("CNPJ");
    dto.setDocumentNumber("98765432000199");
    dto.setAgencia("5678");

    assertEquals(2L, dto.getId());
    assertEquals("CNPJ", dto.getDocumentType());
    assertEquals("98765432000199", dto.getDocumentNumber());
    assertEquals("5678", dto.getAgencia());
  }

  @Test
  void testBuilder() {
    WhitelistDTO dto =
        WhitelistDTO.builder()
            .id(3L)
            .documentType("CPF")
            .documentNumber("00011122233")
            .agencia("9999")
            .build();

    assertEquals(3L, dto.getId());
    assertEquals("CPF", dto.getDocumentType());
    assertEquals("00011122233", dto.getDocumentNumber());
    assertEquals("9999", dto.getAgencia());
  }

  @Test
  void testEqualsAndHashCode() {
    WhitelistDTO dto1 = new WhitelistDTO(1L, "CPF", "12345678901", "1234");
    WhitelistDTO dto2 = new WhitelistDTO(1L, "CPF", "12345678901", "1234");

    assertEquals(dto1, dto2);
    assertEquals(dto1.hashCode(), dto2.hashCode());
  }

  @Test
  void testToString() {
    WhitelistDTO dto = new WhitelistDTO(5L, "CPF", "11122233344", "0001");
    String result = dto.toString();

    assertTrue(result.contains("11122233344"));
    assertTrue(result.contains("0001"));
  }
}
