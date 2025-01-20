package com.santander.bp.app.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.SubProductCosmosDTO;
import com.santander.bp.model.SubProductDetails;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OffersMapperBP82Test {

  private CosmosDbMapper cosmosDbMapper;

  @BeforeEach
  void setUp() {
    cosmosDbMapper = new CosmosDbMapper();
  }

  @Test
  void testMapToOfferResponseDTO() {
    OfferCosmosDTO offer = new OfferCosmosDTO();
    offer.setId("123");
    offer.setProduct("Investment");
    offer.setProductDescription("Fixed Income Product");
    offer.setFamilyCode("FI");
    offer.setSubProducts(List.of(createSubProductCosmosDTO()));

    OffersPricingResponse response = cosmosDbMapper.mapToOfferResponseDTO(offer);

    assertNotNull(response);
    assertEquals("123", response.getId());
    assertEquals("Investment", response.getProduct());
    assertEquals("Fixed Income Product", response.getProductDescription());
    assertEquals("FI", response.getFamilyCode());
    assertNotNull(response.getSubProducts());
    assertEquals(1, response.getSubProducts().size());
  }

  @Test
  void testMapSubProducts() {
    List<SubProductCosmosDTO> subProducts = List.of(createSubProductCosmosDTO());

    List<SubProductDetails> subProductDetails = cosmosDbMapper.mapSubProducts(subProducts);

    assertNotNull(subProductDetails);
    assertEquals(1, subProductDetails.size());
    SubProductDetails details = subProductDetails.get(0);
    assertEquals("Savings", details.getSubProduct());
    assertEquals(1000.0f, details.getMinimumApplicationValue());
    assertEquals(500.0f, details.getMinimumRedeemValue());
    assertEquals(200.0f, details.getMinimumBalanceValue());
    assertEquals("Y", details.getProgressiveRemunerationIndicator());
    assertEquals("CDI", details.getIndexerDescription());
    assertEquals("001", details.getSubCode());
    assertEquals("N", details.getGraceIndicator());
    assertEquals("30", details.getGraceTerm());
  }

  private SubProductCosmosDTO createSubProductCosmosDTO() {
    SubProductCosmosDTO subProduct = new SubProductCosmosDTO();
    subProduct.setNmSubp("Savings");
    subProduct.setVlMiniApli(1000);
    subProduct.setVlMiniResg(500);
    subProduct.setVlMiniSald(200);
    subProduct.setInRemuPgre("Y");
    subProduct.setDsIndx("CDI");
    subProduct.setCdSubp("001");
    subProduct.setInCare("N");
    subProduct.setPzRemuPgre("30");
    return subProduct;
  }
}
