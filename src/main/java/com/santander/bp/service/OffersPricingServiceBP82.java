package com.santander.bp.service;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.exception.AltairException;
import com.santander.bp.factory.Transaction;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Mapeando a requisição para o formato esperado pelo Altair (BPMP82)
    BPMP82 bpmp82 = offersMapperBP82.mapOffersRequest(offersPricingRequest);
    log.info("Dados mapeados para BPMP82: {}", bpmp82);

    // Enviando a requisição para o Altair
    ResponseDto altairReturn = sendMessageAltair(Transaction.BP82, bpmp82);
    log.info("Resposta do Altair recebida: {}", altairReturn);

    // Verificando se a resposta do Altair contém mensagens de erro
    if (altairReturn.getObjeto().getListaMensagem() != null
        && !altairReturn.getObjeto().getListaMensagem().isEmpty()) {

      // Pegando a primeira mensagem de erro para lançar a exceção
      String codigoErro = altairReturn.getObjeto().getListaMensagem().get(0).getCodigo();
      String mensagemErro = altairReturn.getObjeto().getListaMensagem().get(0).getMensagem();

      log.error(
          "Erro retornado pelo Altair MQ. Código: {}, Mensagem: {}", codigoErro, mensagemErro);

      // Lançando uma exceção específica do Altair para ser tratada posteriormente
      throw new AltairException(codigoErro, mensagemErro, "Erro retornado pelo Altair MQ");
    }

    // Se não houver erros, mapear a resposta para o formato esperado (OffersPricingResponse)
    List<OffersPricingResponse> responseList = offersMapperBP82.mapOffersResponseList(altairReturn);
    log.info("Lista de respostas mapeadas para OffersPricingResponse: {}", responseList);

    return responseList;
  }

  private ResponseDto sendMessageAltair(Transaction transaction, Object dados) {
    try {
      log.info(
          "Iniciando envio para Altair MQ. Transação: {}, Dados: {}", transaction.name(), dados);

      arsenalAltairConfig.setTransactionName(transaction.name());
      altairFacade = altairStrategy.getAltairFacade(arsenalAltairConfig, dados);

      Instant startingPoint = Instant.now();
      ResponseDto saida = altairFacade.executeIntegrationAltair();

      long responseTime = Duration.between(startingPoint, Instant.now()).toMillis();
      log.info("MQ response time {} ms", responseTime);
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
}
