package com.santander.bp.service;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.exception.AppError;
import com.santander.bp.exception.RestApiException;
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
      throws RestApiException {

    BPMP82 bpmp82 = offersMapperBP82.mapOffersRequest(offersPricingRequest);
    ResponseDto altairReturn = sendMessageAltair(Transaction.BP82, bpmp82);

    if (altairReturn.getObjeto().getListaMensagem() != null
        && !altairReturn.getObjeto().getListaMensagem().isEmpty()) {
      String codigoErro = altairReturn.getObjeto().getListaMensagem().get(0).getCodigo();
      String mensagemErro = altairReturn.getObjeto().getListaMensagem().get(0).getMensagem();

      if (!"BPA0000".equals(codigoErro)) {
        log.error(
            "Erro retornado pelo Altair MQ. Código: {}, Mensagem: {}", codigoErro, mensagemErro);
        throw new RestApiException(AppError.ALTAIR_ERROR);
      } else {
        log.info("Consulta realizada com sucesso: {}", mensagemErro);
      }
    }

    return offersMapperBP82.mapOffersResponseList(altairReturn);
  }

  private ResponseDto sendMessageAltair(Transaction transaction, Object dados) {
    try {
      log.info(
          "Iniciando envio para Altair MQ. Transação: {}, Dados: {}", transaction.name(), dados);

      arsenalAltairConfig.setTransactionName(transaction.name());
      altairFacade = altairStrategy.getAltairFacade(arsenalAltairConfig, dados);

      Instant startingPoint = Instant.now();
      ResponseDto saida = altairFacade.executeIntegrationAltair();

      log.info("MQ response time {} ms", Duration.between(startingPoint, Instant.now()).toMillis());
      log.info("Resposta recebida do Altair MQ: {}", saida);

      return saida;
    } catch (Exception e) {
      log.error(
          "Erro ao enviar mensagem para Altair MQ. Transação: {}, Dados: {}, Erro: {}",
          transaction.name(),
          dados,
          e.getMessage(),
          e);
      throw new RestApiException(AppError.ALTAIR_ERROR);
    }
  }
}
