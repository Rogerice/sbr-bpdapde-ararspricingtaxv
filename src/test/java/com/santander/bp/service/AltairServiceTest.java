package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsObjectReturn;
import com.santander.bp.enums.TransactionEnum;
import com.santander.bp.exception.RestApiException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AltairServiceTest {

  @Mock private ArsenalAltairConfig arsenalAltairConfig;
  @Mock private AltairStrategy altairStrategy;
  @Mock private AltairFacade altairFacade;
  @Mock private ResponseDto responseDto;
  @Mock private PsObjectReturn psObjectReturn;

  private AltairService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    service = new AltairService(arsenalAltairConfig, altairStrategy);
  }

  @Test
  void testSendMessageAltair_Success() throws Exception {
    when(altairStrategy.getAltairFacade(arsenalAltairConfig, "TestRequest"))
        .thenReturn(altairFacade);
    when(altairFacade.executeIntegrationAltair()).thenReturn(responseDto);

    ResponseDto result = service.sendMessageAltair(TransactionEnum.BP82, "TestRequest");

    assertNotNull(result);
    assertEquals(responseDto, result);
  }

  @Test
  void testHandleBusinessErrorsIfAny_NoErrors() {
    when(responseDto.getObjeto()).thenReturn(psObjectReturn);
    when(psObjectReturn.getListaErros()).thenReturn(Collections.emptyList());

    assertDoesNotThrow(() -> service.handleBusinessErrorsIfAny(responseDto));
  }

  @Test
  void testHandleBusinessErrorsIfAny_WithErrors() {
    PsError psError = new PsError();
    psError.setCodigo("ERR001");
    psError.setMensagem("Erro de teste");

    when(responseDto.getObjeto()).thenReturn(psObjectReturn);
    when(psObjectReturn.getListaErros()).thenReturn(List.of(psError));

    Exception exception =
        assertThrows(RestApiException.class, () -> service.handleBusinessErrorsIfAny(responseDto));

    RestApiException restException = (RestApiException) exception;
    assertEquals("Erro de negócio no Altair", restException.getTitle());
    assertEquals("ERR001", restException.getCode());

    // Converte `getDetails()` para `List<PsError>` antes de acessar
    @SuppressWarnings("unchecked")
    List<PsError> detalhesErro = (List<PsError>) restException.getDetails();

    assertEquals(1, detalhesErro.size());
    assertEquals("Erro de teste", detalhesErro.get(0).getMensagem());
    assertEquals("ERR001", detalhesErro.get(0).getCodigo());
  }
}
