package com.santander.bp.service;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairFacade;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.santander.bp.app.mapper.OffersMapper;
import com.santander.bp.exception.AppError;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.factory.Transaction;
import com.santander.bp.model.OffersAltairRequest;
import com.santander.bp.model.OffersAltairResponse;
import com.santander.bp.model.altair.BPMP82;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OffersService {

  @Autowired private OffersMapper offersMapper;

  @Autowired private ArsenalAltairConfig arsenalAltairConfig;

  @Autowired private AltairStrategy altairStrategy;

  @Autowired private AltairFacade altairFacade;

  public List<OffersAltairResponse> processOffers(OffersAltairRequest offersAltairRequest)
      throws Exception {
    BPMP82 bpmp82 = offersMapper.mapOffersRequest(offersAltairRequest);

    ResponseDto altairReturn = sendMessageAltair(Transaction.BP82, bpmp82);

    if (altairReturn.getObjeto().getListaMensagem() != null
        && !altairReturn.getObjeto().getListaMensagem().isEmpty()) {
      String codigoErro = altairReturn.getObjeto().getListaMensagem().get(0).getCodigo();
      String mensagemErro = altairReturn.getObjeto().getListaMensagem().get(0).getMensagem();

      if (!"BPA0000".equals(codigoErro)) {
        log.error(
            "Erro retornado pelo Altair MQ. Código: {}, Mensagem: {}", codigoErro, mensagemErro);
        throw new RestApiException(AppError.ALTAR_ERROR);
      } else {
        log.info("Consulta realizada com sucesso: {}", mensagemErro);
      }
    }

    return offersMapper.mapOffersResponseList(altairReturn);
  }

  private ResponseDto sendMessageAltair(Transaction transaction, Object dados) {
    try {
      log.info(
          "Iniciando envio para Altair MQ. Transação: {}, Dados: {}", transaction.name(), dados);

      arsenalAltairConfig.setTransactionName(transaction.name());

      log.info("Configuração do Altair realizada: {}", arsenalAltairConfig);

      altairFacade = altairStrategy.getAltairFacade(arsenalAltairConfig, dados);

      log.info("Iniciando comunicação com Altair MQ...");

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
      throw new RestApiException(AppError.CANNOT_SEND_MENSSAGE, e);
    }
  }
}
