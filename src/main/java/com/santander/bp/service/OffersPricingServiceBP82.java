package com.santander.bp.service;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.exception.AltairException;
import com.santander.bp.factory.Transaction;
import com.santander.bp.handler.ResponseHandler;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Generated
@Service
@Slf4j
public class OffersPricingServiceBP82 {

  @Autowired private OffersMapperBP82 offersMapperBP82;
  @Autowired private ArsenalAltairConfig arsenalAltairConfig;
  @Autowired private AltairStrategy altairStrategy;
  @Autowired private AltairFacade altairFacade;

  public List<OffersPricingResponse> processOffers(OffersPricingRequest offersPricingRequest)
      throws AltairException {

    log.info(
        "Iniciando o mapeamento da requisição para BPMP82. Dados recebidos: {}",
        offersPricingRequest);

    List<OffersPricingResponse> responseList = new ArrayList<>();
    boolean recall;

    do {
      BPMP82 bpmp82 = offersMapperBP82.mapOffersRequest(offersPricingRequest);
      log.info("Dados mapeados para BPMP82: {}", bpmp82);

      ResponseDto altairReturn = sendMessageAltair(Transaction.BP82, bpmp82);
      log.info("Resposta do Altair recebida: {}", altairReturn);

      ResponseHandler.handleErrors(altairReturn);
      responseList.addAll(offersMapperBP82.mapOffersResponseList(altairReturn));

      recall = ResponseHandler.shouldRecall(altairReturn, offersPricingRequest);

    } while (recall);

    return responseList;
  }

  private ResponseDto sendMessageAltair(Transaction transaction, Object dados) {
    try {
      log.info(
          "Iniciando envio para Altair MQ. Transação: {}, Dados: {}", transaction.name(), dados);

      configurarArsenalConfig(transaction);
      altairFacade = altairStrategy.getAltairFacade(arsenalAltairConfig, dados);

      Instant startingPoint = Instant.now();
      ResponseDto saida = altairFacade.executeIntegrationAltair();

      logResponseTime(startingPoint);
      log.info("Resposta recebida do Altair MQ: {}", saida);

      return saida;

    } catch (Exception e) {
      log.error(
          "Erro ao enviar mensagem para Altair MQ. Transação: {}, Dados: {}, Erro: {}",
          transaction.name(),
          dados,
          e.getMessage(),
          e);
      throw new AltairException(
          "MQ_ERROR", e.getMessage(), "Erro ao enviar mensagem para o Altair MQ");
    }
  }

  private void configurarArsenalConfig(Transaction transaction) {
    arsenalAltairConfig.setTransactionName(transaction.name());
  }

  private void logResponseTime(Instant startingPoint) {
    long responseTime = Duration.between(startingPoint, Instant.now()).toMillis();
    log.info("MQ response time {} ms", responseTime);
  }
}
