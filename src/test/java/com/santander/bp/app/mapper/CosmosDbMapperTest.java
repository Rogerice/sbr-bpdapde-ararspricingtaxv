package com.santander.bp.app.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.SubProductCosmosDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CosmosDbMapperTest {

  private CosmosDbMapper cosmosDbMapper;

  @BeforeEach
  void setUp() {
    cosmosDbMapper = new CosmosDbMapper();
  }

  @Test
  void testMapToOfferResponseDTO_Success() {
    OfferCosmosDTO offer =
        OfferCosmosDTO.builder()
            .id("ID_1")
            .product("Product A")
            .family("Family A")
            .productDescription("Desc A")
            .subProducts(
                List.of(
                    SubProductCosmosDTO.builder()
                        .nmSubp("Sub A1")
                        .cdSubp("SP001")
                        .vlMiniApli(100)
                        .vlMiniResg(200)
                        .vlMiniSald(300)
                        .inRemuPgre("Y")
                        .dsIndx("INDEX1")
                        .pzRemuPgre("30D")
                        .inCare("N")
                        .build()))
            .build();

    SubProductCosmosDTO bestSubProduct = cosmosDbMapper.getBestSubProduct(offer);

    OffersPricingResponse response = cosmosDbMapper.mapToOfferResponseDTO(offer, bestSubProduct);

    assertNotNull(response);
    assertEquals("Product A", response.getProduct());
    assertEquals("Sub A1", response.getSubProduct());
    assertEquals(100.0, response.getMinApplicationValue());
    assertEquals(200.0, response.getMinRedemptionValue());
    assertEquals(300.0, response.getMinBalance());
  }

  @Test
  void testGetBestSubProduct_ReturnsCorrectly() {
    OfferCosmosDTO offer =
        OfferCosmosDTO.builder()
            .id("ID_1")
            .product("Product A")
            .family("Family A")
            .productDescription("Desc A")
            .subProducts(
                List.of(
                    SubProductCosmosDTO.builder()
                        .nmSubp("Sub A1")
                        .cdSubp("SP001")
                        .vlMiniApli(100)
                        .vlMiniResg(200)
                        .vlMiniSald(300)
                        .build(),
                    SubProductCosmosDTO.builder()
                        .nmSubp("Sub A2")
                        .cdSubp("SP002")
                        .vlMiniApli(50) // O menor
                        // `vlMiniApli()`
                        // é 50
                        .vlMiniResg(150)
                        .vlMiniSald(250)
                        .build()))
            .build();

    SubProductCosmosDTO bestSubProduct = cosmosDbMapper.getBestSubProduct(offer);
    assertNotNull(bestSubProduct);
    assertEquals("SP002", bestSubProduct.getCdSubp());
    assertEquals("Sub A2", bestSubProduct.getNmSubp());
    assertEquals(50, bestSubProduct.getVlMiniApli());
  }

  @Test
  void testGetBestSubProduct_NoSubProductsReturnsNull() {
    OfferCosmosDTO offer =
        OfferCosmosDTO.builder()
            .id("ID_1")
            .product("Product A")
            .family("Family A")
            .productDescription("Desc A")
            .subProducts(List.of())
            .build();

    SubProductCosmosDTO bestSubProduct = cosmosDbMapper.getBestSubProduct(offer);
    assertNull(bestSubProduct);
  }

  @Test
  void testConvertToDouble_Success() {
    assertEquals(10.0, cosmosDbMapper.convertToDouble(10));
    assertEquals(99.0, cosmosDbMapper.convertToDouble(99));
  }

  @Test
  void testConvertToDouble_NullReturnsNull() {
    assertNull(cosmosDbMapper.convertToDouble(null));
  }
}
