package com.santander.bp.handler;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.santander.bp.exception.AltairException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

@Generated
@Slf4j
public final class ResponseHandler {

  private ResponseHandler() {
    throw new UnsupportedOperationException("Esta classe não deve ser instanciada.");
  }

  public static void handleErrors(ResponseDto response) {
    if (isInvalidErrorList(response)) {
      return;
    }

    logErrors(response);
    throw new AltairException("ERRO ALTAIR", "Erro na transação", "Detalhes dos erros encontrados");
  }

  private static boolean isInvalidErrorList(ResponseDto response) {
    return response.getObjeto().getListaErros() == null
        || response.getObjeto().getListaErros().isEmpty();
  }

  private static void logErrors(ResponseDto response) {
    log.warn("Erro retornado altair ");
    for (PsError error : response.getObjeto().getListaErros()) {
      log.warn("Código do Erro: {}, Mensagem do Erro: {}", error.getCodigo(), error.getMensagem());
    }
  }
}
