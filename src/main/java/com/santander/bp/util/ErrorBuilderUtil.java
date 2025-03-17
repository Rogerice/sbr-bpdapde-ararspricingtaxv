package com.santander.bp.util;

import com.santander.bp.model.Error;
import com.santander.bp.model.ErrorLevel;
import com.santander.bp.model.Errors;
import java.util.Collections;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;

@Generated
public class ErrorBuilderUtil {

  private static final String ERROR_500 = "500";
  private static final String ERROR_404 = "NOT_FOUND";
  private static final String ERROR_MSG_SERVER = "Internal Server Error";
  private static final String ERROR_MSG_NOT_FOUND = "No offers found";

  /**
   * Método genérico para construir uma instância de Errors.
   *
   * @param code Código do erro (exemplo: "500", "NOT_FOUND").
   * @param message Mensagem resumida do erro.
   * @param description Descrição detalhada do erro.
   * @param level Nível do erro (INFO, WARNING, ERROR).
   * @return Objeto Errors contendo a estrutura do erro.
   */
  public static Errors buildError(
      String code, String message, String description, ErrorLevel level) {
    if (StringUtils.isAnyBlank(code, message, description) || level == null) {
      throw new IllegalArgumentException("Parâmetros inválidos para construir o erro");
    }

    Error error =
        Error.builder().code(code).message(message).level(level).description(description).build();

    return Errors.builder().errors(Collections.singletonList(error)).build();
  }

  /**
   * Método específico para erro de servidor (500).
   *
   * @param description Detalhes do erro.
   * @return Instância de Errors representando erro interno do servidor.
   */
  public static Errors buildServerError(String description) {
    return buildError(ERROR_500, ERROR_MSG_SERVER, description, ErrorLevel.ERROR);
  }

  /**
   * Método específico para erro de "Não encontrado" (404).
   *
   * @param description Detalhes do erro.
   * @return Instância de Errors representando erro de recurso não encontrado.
   */
  public static Errors buildNotFoundError(String description) {
    return buildError(ERROR_404, ERROR_MSG_NOT_FOUND, description, ErrorLevel.INFO);
  }
}
