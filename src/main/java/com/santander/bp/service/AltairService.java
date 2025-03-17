package com.santander.bp.service;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.santander.bp.enums.TransactionEnum;
import com.santander.bp.exception.AltairException;
import com.santander.bp.exception.RestApiException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AltairService {

  private final ArsenalAltairConfig arsenalAltairConfig;
  private final AltairStrategy altairStrategy;

  public AltairService(ArsenalAltairConfig arsenalAltairConfig, AltairStrategy altairStrategy) {
    this.arsenalAltairConfig = arsenalAltairConfig;
    this.altairStrategy = altairStrategy;
  }

  public ResponseDto sendMessageAltair(TransactionEnum transactionEnum, Object request) {
    log.info("Enviando mensagem para Altair MQ. Transação: {}", transactionEnum.name());

    try {
      arsenalAltairConfig.setTransactionName(transactionEnum.name());
      AltairFacade altairFacade = altairStrategy.getAltairFacade(arsenalAltairConfig, request);

      Instant startTime = Instant.now();
      ResponseDto response = altairFacade.executeIntegrationAltair();
      logResponseTime(startTime);

      log.debug("Resposta recebida do Altair MQ: {}", response);
      return response;
    } catch (Exception e) {
      log.error("Erro ao enviar mensagem para Altair MQ: {}", e.getMessage(), e);
      throw new AltairException("ALT001", e.getMessage(), "Erro ao comunicar com Altair MQ", e);
    }
  }

  private void logResponseTime(Instant startTime) {
    long responseTime = Duration.between(startTime, Instant.now()).toMillis();
    log.info("Tempo de resposta do Altair MQ: {} ms", responseTime);
  }

  protected void handleBusinessErrorsIfAny(ResponseDto responseDto) {
    Optional.ofNullable(responseDto.getObjeto().getListaErros())
        .filter(errors -> !errors.isEmpty())
        .ifPresent(
            errors -> {
              String errorMessages =
                  errors.stream()
                      .map(
                          error ->
                              String.format(
                                  "[Código: %s, Mensagem: %s]",
                                  error.getCodigo(), error.getMensagem()))
                      .reduce("", (a, b) -> a + " " + b);

              String firstErrorMessage = errors.get(0).getMensagem();
              String errorCode = errors.get(0).getCodigo();

              log.warn("Erro de negócio no Altair MQ: {}", errorMessages);

              throw new RestApiException(
                  HttpStatus.BAD_REQUEST,
                  errorCode,
                  "Erro de negócio no Altair",
                  firstErrorMessage,
                  errors);
            });
  }
}
