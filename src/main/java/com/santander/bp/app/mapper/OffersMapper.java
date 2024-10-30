package com.santander.bp.app.mapper;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.model.OffersAltairRequest;
import com.santander.bp.model.OffersAltairResponse;
import com.santander.bp.model.altair.BPMP82;
import com.santander.bp.model.altair.BPMP820;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OffersMapper {

  public BPMP82 mapOffersRequest(OffersAltairRequest offersAltairRequest) {
    BPMP82 bpmp82 = new BPMP82();
    bpmp82.setBANCO(offersAltairRequest.getBanco());
    bpmp82.setCANAL(offersAltairRequest.getCanal());
    bpmp82.setPRODUTO(offersAltairRequest.getProduto());
    bpmp82.setSUBPROD(offersAltairRequest.getSubProd());
    bpmp82.setFAMILIA(offersAltairRequest.getFamilia());
    bpmp82.setPENUMPE(offersAltairRequest.getPenumper());
    bpmp82.setSEGMENT(offersAltairRequest.getSegment());
    bpmp82.setTPFUNC(offersAltairRequest.getTpfFunc());
    bpmp82.setINDREA(offersAltairRequest.getIndRea());
    bpmp82.setPRODREA(offersAltairRequest.getProdRea());
    bpmp82.setSUBPREA(offersAltairRequest.getSubPrea());
    return bpmp82;
  }

  public List<OffersAltairResponse> mapOffersResponseList(ResponseDto responseDto) {
    List<OffersAltairResponse> offersResponseList = new ArrayList<>();

    log.info("ResponseDto recebido: {}", responseDto);
    log.info("Lista de Erros: {}", responseDto.getObjeto().getListaErros());
    log.info("Lista de Mensagens: {}", responseDto.getObjeto().getListaMensagem());
    log.info("Lista de Formatos: {}", responseDto.getObjeto().getListaFormatos());

    if (responseDto.getObjeto().getListaErros() != null
        && !responseDto.getObjeto().getListaErros().isEmpty()) {
      log.warn("Lista de erros encontrada no ResponseDto:");

      responseDto
          .getObjeto()
          .getListaErros()
          .forEach(
              erro -> {
                if (erro instanceof PsError) {
                  PsError psError = (PsError) erro;
                  log.warn(
                      "Código do Erro: {}, Mensagem do Erro: {}",
                      psError.getCodigo(),
                      psError.getMensagem());
                } else {
                  log.warn("Erro desconhecido: {}", erro);
                }
              });

      return offersResponseList;
    }

    if (responseDto.getObjeto().getListaMensagem() != null
        && !responseDto.getObjeto().getListaMensagem().isEmpty()) {
      log.info("Iniciando mapeamento da lista de formatos...");

      responseDto
          .getObjeto()
          .getListaFormatos()
          .forEach(
              format -> {
                if (format instanceof PsScreen) {
                  PsScreen psScreen = (PsScreen) format;
                  if (psScreen.getFormato() instanceof BPMP820) {
                    BPMP820 bpmp820 = (BPMP820) psScreen.getFormato();

                    OffersAltairResponse offersAltairResponse =
                        construirOffersAltairResponse(bpmp820);
                    log.info("OffersAltairResponse criado: {}", offersAltairResponse);
                    offersResponseList.add(offersAltairResponse);
                  } else {
                    log.error("O formato não é uma instância de BPMP820");
                  }
                } else {
                  log.error("O objeto format não é uma instância de PsScreen");
                }
              });

      log.info(
          "Mapeamento concluído. Tamanho da lista de respostas: {}", offersResponseList.size());
    }

    return offersResponseList;
  }

  private OffersAltairResponse construirOffersAltairResponse(BPMP820 bpmp820) {
    return OffersAltairResponse.builder()
        .produto(bpmp820.getPRODUTO())
        .subProd(bpmp820.getSUBPROD())
        .desSubp(bpmp820.getDESSUBP())
        .familia(bpmp820.getFAMILIA())
        .indPrg(bpmp820.getINDPRG())
        .otrPraz(bpmp820.getOTRPRAZ())
        .inApli(bpmp820.getINAPLI())
        .inResg(bpmp820.getINRESG())
        .agnFutu(bpmp820.getAGNFUTU())
        .agnResg(bpmp820.getAGNRESG())
        .hrOnlin(bpmp820.getHRONLIN())
        .prazo1(bpmp820.getPRAZO1())
        .prazo2(bpmp820.getPRAZO2())
        .prazo3(bpmp820.getPRAZO3())
        .vlrMinA(bpmp820.getVLRMINA())
        .taxaEnc(bpmp820.getTAXAENC())
        .build();
  }
}
