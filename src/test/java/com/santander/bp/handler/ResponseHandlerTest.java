package com.santander.bp.handler;

import static org.junit.jupiter.api.Assertions.*;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsObjectReturn;
import com.santander.bp.model.OffersPricingRequest;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ResponseHandlerTest {

  @Test
  public void testShouldRecall_InvalidFormatList() {
    ResponseDto response = Mockito.mock(ResponseDto.class);
    PsObjectReturn psObjectReturn = Mockito.mock(PsObjectReturn.class);
    Mockito.when(response.getObjeto()).thenReturn(psObjectReturn);
    Mockito.when(psObjectReturn.getListaFormatos()).thenReturn(Collections.emptyList());
    OffersPricingRequest request = new OffersPricingRequest();

    boolean result = ResponseHandler.shouldRecall(response, request);

    assertFalse(result, "Expected shouldRecall to return false when format list is empty");
  }

  @Test
  public void testHandleErrors_InvalidErrorList() {
    ResponseDto response = Mockito.mock(ResponseDto.class);
    PsObjectReturn psObjectReturn = Mockito.mock(PsObjectReturn.class);
    Mockito.when(response.getObjeto()).thenReturn(psObjectReturn);
    Mockito.when(psObjectReturn.getListaErros()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(
        () -> ResponseHandler.handleErrors(response),
        "Expected handleErrors not to throw exception when error list is empty");
  }
}
