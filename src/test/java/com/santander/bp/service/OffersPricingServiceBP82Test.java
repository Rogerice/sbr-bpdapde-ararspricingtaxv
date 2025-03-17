package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsObjectReturn;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OffersPricingServiceBP82Test {

  @Mock private ArsenalAltairConfig arsenalAltairConfig;
  @Mock private AltairStrategy altairStrategy;
  @Mock private OffersMapperBP82 offersMapperBP82;
  @Mock private OffersPricingRequest offersPricingRequest;
  @Mock private ResponseDto responseDto;
  @Mock private BPMP82 bpmp82;
  @Mock private PsObjectReturn psObjectReturn;
  @Mock private AltairFacade altairFacade;

  private OffersPricingServiceBP82 service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    service = new OffersPricingServiceBP82(arsenalAltairConfig, altairStrategy, offersMapperBP82);
  }

  @Test
  void testProcessOffers_Success() throws Exception {
    when(offersMapperBP82.mapOffersRequest(offersPricingRequest)).thenReturn(bpmp82);

    when(altairStrategy.getAltairFacade(arsenalAltairConfig, bpmp82)).thenReturn(altairFacade);
    when(altairFacade.executeIntegrationAltair()).thenReturn(responseDto);

    when(responseDto.getObjeto()).thenReturn(psObjectReturn);
    when(psObjectReturn.getListaErros()).thenReturn(Collections.emptyList());
    List<OffersPricingResponse> expectedResponses = List.of(new OffersPricingResponse());
    when(offersMapperBP82.mapOffersResponseList(responseDto)).thenReturn(expectedResponses);

    List<OffersPricingResponse> result = service.processOffers(offersPricingRequest);

    assertNotNull(result);
    assertEquals(expectedResponses, result);

    verify(offersMapperBP82).mapOffersRequest(offersPricingRequest);
    verify(altairStrategy).getAltairFacade(arsenalAltairConfig, bpmp82);
    verify(altairFacade).executeIntegrationAltair();
    verify(responseDto).getObjeto();
    verify(psObjectReturn).getListaErros();
    verify(offersMapperBP82).mapOffersResponseList(responseDto);
  }

  @Test
  void testProcessOffers_BusinessError() throws Exception {
    when(offersMapperBP82.mapOffersRequest(offersPricingRequest)).thenReturn(bpmp82);

    when(altairStrategy.getAltairFacade(arsenalAltairConfig, bpmp82)).thenReturn(altairFacade);
    when(altairFacade.executeIntegrationAltair()).thenReturn(responseDto);

    PsError psError = new PsError();
    psError.setCodigo("ERR001");
    psError.setMensagem("Erro de teste");

    when(responseDto.getObjeto()).thenReturn(psObjectReturn);
    when(psObjectReturn.getListaErros()).thenReturn(List.of(psError));

    Exception exception =
        assertThrows(RestApiException.class, () -> service.processOffers(offersPricingRequest));

    RestApiException restException = (RestApiException) exception;
    assertEquals("Erro de negócio no Altair", restException.getTitle());
    assertEquals("ERR001", restException.getCode());

    verify(offersMapperBP82).mapOffersRequest(offersPricingRequest);
    verify(altairStrategy).getAltairFacade(arsenalAltairConfig, bpmp82);
    verify(altairFacade).executeIntegrationAltair();
    // verify(service).handleBusinessErrorsIfAny(responseDto);
  }

  @Test
  void testProcessOffers_NullRequest() {
    Exception exception =
        assertThrows(NullPointerException.class, () -> service.processOffers(null));

    assertEquals("OffersPricingRequest não pode ser nulo", exception.getMessage());
  }
}
