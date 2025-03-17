package com.santander.bp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import org.junit.jupiter.api.Test;

public class OfferCosmosDTOTest {

  @Test
  public void testOfferCosmosDTOBuilder() {
    SubProductCosmosDTO subProduct = new SubProductCosmosDTO();
    OfferCosmosDTO offer =
        OfferCosmosDTO.builder()
            .id("1")
            .product("Product1")
            .productDescription("Product Description")
            .family("Family1")
            .channelCode("Channel1")
            .cdSegm("Segment1")
            .tpSegm("Type1")
            .subProducts(Collections.singletonList(subProduct))
            .build();

    assertNotNull(offer);
    assertEquals("1", offer.getId());
    assertEquals("Product1", offer.getProduct());
    assertEquals("Product Description", offer.getProductDescription());
    assertEquals("Family1", offer.getFamily());
    assertEquals("Channel1", offer.getChannelCode());
    assertEquals("Segment1", offer.getCdSegm());
    assertEquals("Type1", offer.getTpSegm());
    assertNotNull(offer.getSubProducts());
    assertEquals(1, offer.getSubProducts().size());
    assertEquals(subProduct, offer.getSubProducts().get(0));
  }

  @Test
  public void testOfferCosmosDTOSetters() {
    SubProductCosmosDTO subProduct = new SubProductCosmosDTO();
    OfferCosmosDTO offer = new OfferCosmosDTO();
    offer.setId("1");
    offer.setProduct("Product1");
    offer.setProductDescription("Product Description");
    offer.setFamily("Family1");
    offer.setChannelCode("Channel1");
    offer.setCdSegm("Segment1");
    offer.setTpSegm("Type1");
    offer.setSubProducts(Collections.singletonList(subProduct));

    assertNotNull(offer);
    assertEquals("1", offer.getId());
    assertEquals("Product1", offer.getProduct());
    assertEquals("Product Description", offer.getProductDescription());
    assertEquals("Family1", offer.getFamily());
    assertEquals("Channel1", offer.getChannelCode());
    assertEquals("Segment1", offer.getCdSegm());
    assertEquals("Type1", offer.getTpSegm());
    assertNotNull(offer.getSubProducts());
    assertEquals(1, offer.getSubProducts().size());
    assertEquals(subProduct, offer.getSubProducts().get(0));
  }
}
