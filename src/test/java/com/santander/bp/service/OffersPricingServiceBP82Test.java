package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.exception.AltairException;
import com.santander.bp.handler.ResponseHandler;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class OffersPricingServiceBP82Test {

  @Mock private OffersMapperBP82 offersMapperBP82;
  @Mock private ArsenalAltairConfig arsenalAltairConfig;
  @Mock private AltairStrategy altairStrategy;
  @Mock private AltairFacade altairFacade;

  @InjectMocks private OffersPricingServiceBP82 offersPricingServiceBP82;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testProcessOffers_Success() throws Exception {
    OffersPricingRequest request = createOffersPricingRequest();
    BPMP82 bpmp82 = createBPMP82();
    ResponseDto responseDto = createResponseDto();
    List<OffersPricingResponse> mappedResponses = List.of(createOffersPricingResponse());

    when(offersMapperBP82.mapOffersRequest(request)).thenReturn(bpmp82);
    when(offersMapperBP82.mapOffersResponseList(responseDto)).thenReturn(mappedResponses);
    when(altairStrategy.getAltairFacade(arsenalAltairConfig, bpmp82)).thenReturn(altairFacade);
    when(altairFacade.executeIntegrationAltair()).thenReturn(responseDto);

    try (MockedStatic<ResponseHandler> mockedStatic = Mockito.mockStatic(ResponseHandler.class)) {
      mockedStatic
          .when(() -> ResponseHandler.handleErrors(responseDto))
          .thenAnswer(invocation -> null);
      mockedStatic.when(() -> ResponseHandler.shouldRecall(responseDto, request)).thenReturn(false);

      List<OffersPricingResponse> result = offersPricingServiceBP82.processOffers(request);

      assertNotNull(result);
      assertEquals(1, result.size());
      assertEquals("Produto1", result.get(0).getProduct());

      verify(offersMapperBP82, times(1)).mapOffersRequest(request);
      verify(offersMapperBP82, times(1)).mapOffersResponseList(responseDto);
    }
  }

  @Test
  void testSendMessageAltair_Error() throws Exception {
    BPMP82 bpmp82 = createBPMP82();

    when(altairStrategy.getAltairFacade(arsenalAltairConfig, bpmp82)).thenReturn(altairFacade);
    doThrow(new RuntimeException("Erro de MQ")).when(altairFacade).executeIntegrationAltair();

    AltairException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            AltairException.class,
            () -> offersPricingServiceBP82.processOffers(createOffersPricingRequest()));

    assertEquals("MQ_ERROR", exception.getCode());
  }

  private OffersPricingRequest createOffersPricingRequest() {
    OffersPricingRequest request = new OffersPricingRequest();
    request.setChannel("WEB");
    request.setCustomerId("12345");
    request.setSubProduct("SP01");
    request.setProductFamily("FAM01");
    request.setSegment("SEG01");
    return request;
  }

  private BPMP82 createBPMP82() {
    BPMP82 bpmp82 = new BPMP82();
    bpmp82.setCANAL("WEB");
    bpmp82.setPENUMPE("12345");
    return bpmp82;
  }

  private ResponseDto createResponseDto() {
    ResponseDto responseDto = new ResponseDto();
    return responseDto; // Configure como necessário
  }

  private OffersPricingResponse createOffersPricingResponse() {
    OffersPricingResponse response = new OffersPricingResponse();
    response.setProduct("Produto1");
    return response;
  }
}
