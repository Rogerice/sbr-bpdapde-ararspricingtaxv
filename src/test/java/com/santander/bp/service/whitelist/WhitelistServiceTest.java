package com.santander.bp.service.whitelist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsObjectReturn;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.SubProductDetails;
import com.santander.bp.model.altair.BPMP82;
import com.santander.bp.model.altair.BPMP820;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WhitelistServiceTest {

  private OffersMapperBP82 offersMapperBP82;

  @BeforeEach
  void setUp() {
    offersMapperBP82 = new OffersMapperBP82();
  }

  @Test
  void testMapOffersRequest() {
    OffersPricingRequest request = new OffersPricingRequest();
    request.setChannel("WEB");
    request.setSubProduct("123");
    request.setProductFamily("FAM001");
    request.setCustomerId("CUST123");
    request.setSegment("SEG001");
    request.setFuncType("A");
    request.setIndicatorRecall("Y");
    request.setCdRecall("RC123");
    request.setSubProductRecall("SUBRC");

    BPMP82 bpmp82 = offersMapperBP82.mapOffersRequest(request);

    assertNotNull(bpmp82);
    assertEquals("WEB", bpmp82.getCANAL());
    assertEquals("123", bpmp82.getSUBPROD());
    assertEquals("FAM001", bpmp82.getFAMILIA());
    assertEquals("CUST123", bpmp82.getPENUMPE());
    assertEquals("SEG001", bpmp82.getSEGMENT());
    assertEquals("A", bpmp82.getTPFUNC());
    assertEquals("Y", bpmp82.getINDREA());
    assertEquals("RC123", bpmp82.getPRODREA());
    assertEquals("SUBRC", bpmp82.getSUBPREA());
  }

  @Test
  void testMapOffersResponseList() {
    ResponseDto responseDto = Mockito.mock(ResponseDto.class);
    PsScreen psScreen = Mockito.mock(PsScreen.class);
    BPMP820 bpmp820 = createMockBPMP820();
    PsObjectReturn mockObjectReturn = Mockito.mock(PsObjectReturn.class); // Mock para getObjeto()

    Mockito.when(psScreen.getFormato()).thenReturn(bpmp820);
    Mockito.when(mockObjectReturn.getListaFormatos()).thenReturn(List.of(psScreen));
    Mockito.when(responseDto.getObjeto())
        .thenReturn(mockObjectReturn); // Certifique-se de que getObjeto() retorne
    // o mock

    List<OffersPricingResponse> responseList = offersMapperBP82.mapOffersResponseList(responseDto);

    assertNotNull(responseList);
    assertEquals(1, responseList.size());

    OffersPricingResponse response = responseList.get(0);
    assertEquals("PROD001", response.getProduct());
    assertEquals("Descrição Produto", response.getProductDescription());
    assertEquals("FAM001", response.getFamilyCode());

    List<SubProductDetails> subProducts = response.getSubProducts();
    assertNotNull(subProducts);
    assertEquals(1, subProducts.size());
    SubProductDetails subProduct = subProducts.get(0);
    assertEquals("SUB001", subProduct.getSubProduct());
    assertEquals(1000.0f, subProduct.getMinimumApplicationValue());
    assertEquals(500.0f, subProduct.getMinimumRedeemValue());
    assertEquals("N", subProduct.getGraceIndicator());
  }

  @Test
  void testHandleErrors() {
    ResponseDto responseDto = Mockito.mock(ResponseDto.class);
    PsError psError = Mockito.mock(PsError.class);
    PsObjectReturn mockObjectReturn = Mockito.mock(PsObjectReturn.class); // Mock para getObjeto()

    Mockito.when(psError.getCodigo()).thenReturn("404");
    Mockito.when(psError.getMensagem()).thenReturn("Not Found");
    Mockito.when(mockObjectReturn.getListaErros()).thenReturn(List.of(psError));
    Mockito.when(responseDto.getObjeto())
        .thenReturn(mockObjectReturn); // Certifique-se de que getObjeto() retorne
    // o mock

    offersMapperBP82.mapOffersResponseList(responseDto);

    Mockito.verify(psError, Mockito.times(1)).getCodigo();
    Mockito.verify(psError, Mockito.times(1)).getMensagem();
  }

  private BPMP820 createMockBPMP820() {
    BPMP820 bpmp820 = new BPMP820();
    bpmp820.setPRODUTO("PROD001");
    bpmp820.setDESSUBP("Descrição Produto");
    bpmp820.setFAMILIA("FAM001");
    bpmp820.setSUBPROD("SUB001");
    bpmp820.setVLRMINA(1000.0);
    bpmp820.setVLRMINR(500.0);
    bpmp820.setSLDMIN(200.0);
    bpmp820.setINCARE("N");
    bpmp820.setINDPRG("IND123");
    bpmp820.setPRZCARE(30);
    return bpmp820;
  }
}
