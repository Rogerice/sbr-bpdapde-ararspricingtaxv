package com.santander.bp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SubProductCosmosDTOTest {

  @Test
  public void testSubProductCosmosDTOBuilder() {
    SubProductCosmosDTO subProduct =
        SubProductCosmosDTO.builder()
            .nmSubp("SubProduct1")
            .cdSubp("Code1")
            .vlMiniApli(100)
            .vlMiniResg(200)
            .vlMiniSald(300)
            .inRemuPgre("Remu1")
            .dsIndx("Index1")
            // .pzRemuPgre("Pz1")
            .inCare("Care1")
            .build();

    assertNotNull(subProduct);
    assertEquals("SubProduct1", subProduct.getNmSubp());
    assertEquals("Code1", subProduct.getCdSubp());
    assertEquals(100, subProduct.getVlMiniApli());
    assertEquals(200, subProduct.getVlMiniResg());
    assertEquals(300, subProduct.getVlMiniSald());
    assertEquals("Remu1", subProduct.getInRemuPgre());
    assertEquals("Index1", subProduct.getDsIndx());
    assertEquals("Pz1", subProduct.getPzRemuPgre());
    assertEquals("Care1", subProduct.getInCare());
  }

  @Test
  public void testSubProductCosmosDTOSetters() {
    SubProductCosmosDTO subProduct = new SubProductCosmosDTO();
    subProduct.setNmSubp("SubProduct1");
    subProduct.setCdSubp("Code1");
    subProduct.setVlMiniApli(100);
    subProduct.setVlMiniResg(200);
    subProduct.setVlMiniSald(300);
    subProduct.setInRemuPgre("Remu1");
    subProduct.setDsIndx("Index1");
    //  subProduct.setPzRemuPgre("Pz1");
    subProduct.setInCare("Care1");

    assertNotNull(subProduct);
    assertEquals("SubProduct1", subProduct.getNmSubp());
    assertEquals("Code1", subProduct.getCdSubp());
    assertEquals(100, subProduct.getVlMiniApli());
    assertEquals(200, subProduct.getVlMiniResg());
    assertEquals(300, subProduct.getVlMiniSald());
    assertEquals("Remu1", subProduct.getInRemuPgre());
    assertEquals("Index1", subProduct.getDsIndx());
    assertEquals("Pz1", subProduct.getPzRemuPgre());
    assertEquals("Care1", subProduct.getInCare());
  }
}
