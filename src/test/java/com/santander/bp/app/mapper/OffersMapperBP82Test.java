package com.santander.bp.app.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsObjectReturn;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.enums.FixedFieldsEnum;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;
import com.santander.bp.model.altair.BPMP820;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OffersMapperBP82Test {

  @InjectMocks private OffersMapperBP82 offersMapperBP82;

  private OffersPricingRequest request;
  private BPMP820 bpmp820;

  @BeforeEach
  void setUp() {
    request = new OffersPricingRequest();
    request.setChannel("APP");
    request.setSubProduct("SUB1");
    request.setProductFamily("FAMILY");
    request.setCustomerId("123456");
    request.setSegment("VIP");
    request.setFuncType("A");
    request.setIndicatorRecall("N");
    request.setCdRecall("RECALL");
    request.setSubProductRecall("SUBRECALL");

    bpmp820 = new BPMP820();
    bpmp820.setPRODUTO("INVEST");
    bpmp820.setSUBPROD("SUB1");
    bpmp820.setDESSUBP("Descrição");
    bpmp820.setFAMILIA("FAMILY");
    bpmp820.setVLRMINA(1000.0);
    bpmp820.setVLRMINR(500.0);
    bpmp820.setSLDMIN(200.0);
    bpmp820.setTAXAENC(0.05);
    bpmp820.setTAXAREC(0.02);
    bpmp820.setDESTAXA("Taxa Teste");
    bpmp820.setPRAZO1(30);
    bpmp820.setPRAZO2(60);
    bpmp820.setPRAZO3(90);
    bpmp820.setPRZMIN(15);
    bpmp820.setPRZMAX(180);
  }

  @Test
  void testMapOffersRequest() {
    BPMP82 result = offersMapperBP82.mapOffersRequest(request);

    assertNotNull(result);
    assertEquals(FixedFieldsEnum.BANCO.getValue(), result.getBANCO());
    assertEquals(request.getChannel(), result.getCANAL());
    assertEquals(FixedFieldsEnum.PRODUTO.getValue(), result.getPRODUTO());
    assertEquals(request.getSubProduct(), result.getSUBPROD());
  }

  @Test
  void testMapOffersResponseList_Success() {
    ResponseDto responseDto = new ResponseDto();
    responseDto.setObjeto(new PsObjectReturn());
    PsScreen psScreen = new PsScreen();
    psScreen.setFormato(bpmp820);
    responseDto.getObjeto().setListaFormatos(List.of(psScreen));

    List<OffersPricingResponse> result = offersMapperBP82.mapOffersResponseList(responseDto);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(bpmp820.getPRODUTO(), result.get(0).getProduct());
  }

  @Test
  void testMapOffersResponseList_WithErrors() {
    ResponseDto responseDto = new ResponseDto();
    responseDto.setObjeto(new PsObjectReturn());
    PsError psError = new PsError();
    psError.setCodigo("400");
    psError.setMensagem("Erro genérico");
    responseDto.getObjeto().setListaErros(List.of(psError));

    List<OffersPricingResponse> result = offersMapperBP82.mapOffersResponseList(responseDto);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testConstruirOffersPricingResponse() {
    OffersPricingResponse response = offersMapperBP82.construirOffersPricingResponse(bpmp820);

    assertNotNull(response);
    assertEquals(bpmp820.getPRODUTO(), response.getProduct());
    assertEquals(bpmp820.getSUBPROD(), response.getSubProductCode());
    assertEquals(bpmp820.getDESSUBP().trim(), response.getSubProduct());
    assertEquals(bpmp820.getFAMILIA().trim(), response.getFamily());
    assertEquals(bpmp820.getDESTAXA().trim(), response.getFeeDescription());

    assertEquals(15, response.getTerm());
  }
}
