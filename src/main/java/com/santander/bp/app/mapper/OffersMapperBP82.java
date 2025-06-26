package com.santander.bp.app.mapper;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.enums.FixedFieldsEnum;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.OffersPricingResponseLegacyFlags;
import com.santander.bp.model.RateTerm;
import com.santander.bp.model.altair.BPMP82;
import com.santander.bp.model.altair.BPMP820;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Generated
@Slf4j
@Component
public class OffersMapperBP82 {

  public BPMP82 mapOffersRequest(OffersPricingRequest offersPricingRequest) {
    BPMP82 bpmp82 = new BPMP82();
    bpmp82.setBANCO(FixedFieldsEnum.BANCO.getValue());
    bpmp82.setCANAL(offersPricingRequest.getChannel());
    bpmp82.setPRODUTO(FixedFieldsEnum.PRODUTO.getValue());
    bpmp82.setSUBPROD(offersPricingRequest.getSubProduct());
    bpmp82.setFAMILIA(offersPricingRequest.getProductFamily());
    bpmp82.setPENUMPE(offersPricingRequest.getCustomerId());
    bpmp82.setSEGMENT(offersPricingRequest.getSegment());
    bpmp82.setTPFUNC(offersPricingRequest.getFuncType()); // A ou R
    bpmp82.setINDREA(offersPricingRequest.getIndicatorRecall());
    bpmp82.setPRODREA(offersPricingRequest.getCdRecall());
    bpmp82.setSUBPREA(offersPricingRequest.getSubProductRecall());
    return bpmp82;
  }

  public List<OffersPricingResponse> mapOffersResponseList(ResponseDto responseDto) {
    List<OffersPricingResponse> offersResponseList = new ArrayList<>();

    log.info("ResponseDto recebido: {}", responseDto);

    handleErrors(responseDto);

    if (responseDto.getObjeto().getListaFormatos() != null
        && !responseDto.getObjeto().getListaFormatos().isEmpty()) {
      responseDto
          .getObjeto()
          .getListaFormatos()
          .forEach(
              format -> {
                if (format instanceof PsScreen) {
                  PsScreen psScreen = (PsScreen) format;
                  if (psScreen.getFormato() instanceof BPMP820) {
                    BPMP820 bpmp820 = (BPMP820) psScreen.getFormato();
                    OffersPricingResponse offersPricingResponse =
                        construirOffersPricingResponse(bpmp820);
                    offersResponseList.add(offersPricingResponse);
                  }
                }
              });
    }

    return offersResponseList;
  }

  private void handleErrors(ResponseDto responseDto) {
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
                }
              });
    }
  }

  public OffersPricingResponse construirOffersPricingResponse(BPMP820 bpmp820) {
    OffersPricingResponse response = new OffersPricingResponse();

    response.setProduct(bpmp820.getPRODUTO());
    response.setSubProductCode(bpmp820.getSUBPROD());
    response.setSubProduct(bpmp820.getDESSUBP().trim());
    response.setFamily(bpmp820.getFAMILIA().trim());

    response.setMinApplicationValue(safeConvertDouble(bpmp820.getVLRMINA()));
    response.setMinRedemptionValue(safeConvertDouble(bpmp820.getVLRMINR()));
    response.setMinBalance(safeConvertDouble(bpmp820.getSLDMIN()));
    response.setClosingFee(safeConvertDouble(bpmp820.getTAXAENC()));
    response.setReceivingFee(safeConvertDouble(bpmp820.getTAXAREC()));
    response.setFeeDescription(bpmp820.getDESTAXA().trim());
    response.setGracePeriod(bpmp820.getPRZCARE());

    response.setMessages(buildMessages(bpmp820));

    response.setRateTerm(buildRateDetails(bpmp820));

    response.setTerm(determineMainTerm(bpmp820));
    OffersPricingResponseLegacyFlags legacyFlags = new OffersPricingResponseLegacyFlags();

    legacyFlags.setOtrPraz(bpmp820.getOTRPRAZ()); // indicador de que não é permitido Canal
    legacyFlags.setInApli(
        bpmp820.getINAPLI()); // indicador de permissão se o canal é permitido fazer programação
    legacyFlags.setInResg(bpmp820.getINRESG()); // caracteristica de resgate do canal
    legacyFlags.setAgnFutu(
        bpmp820.getAGNFUTU()); // indicador de caracteristica de agendamento aplicação
    legacyFlags.setAgnResg(
        bpmp820.getAGNRESG()); // indicador de caracteristica de agendamento Resgate
    legacyFlags.setHronlin(bpmp820.getHRONLIN()); // indicador é hardcode, 20h

    response.setLegacyFlags(legacyFlags);
    return response;
  }

  private List<String> buildMessages(BPMP820 bpmp820) {
    List<String> messages = new ArrayList<>();

    if (bpmp820.getMENSAG1() != null && !bpmp820.getMENSAG1().trim().isEmpty()) {
      messages.add(bpmp820.getMENSAG1().trim());
    }
    if (bpmp820.getMENSAG2() != null && !bpmp820.getMENSAG2().trim().isEmpty()) {
      messages.add(bpmp820.getMENSAG2().trim());
    }
    if (bpmp820.getMENSAG3() != null && !bpmp820.getMENSAG3().trim().isEmpty()) {
      messages.add(bpmp820.getMENSAG3().trim());
    }

    return messages.isEmpty() ? null : messages;
  }

  private List<RateTerm> buildRateDetails(BPMP820 bpmp820) {
    Set<String> uniqueRates = new HashSet<>();
    List<RateTerm> rateDetailsList = new ArrayList<>();

    addRateDetailIfValid(uniqueRates, rateDetailsList, bpmp820.getPRAZO1(), bpmp820.getTAXAENC());
    addRateDetailIfValid(uniqueRates, rateDetailsList, bpmp820.getPRAZO2(), bpmp820.getTAXAENC());
    addRateDetailIfValid(uniqueRates, rateDetailsList, bpmp820.getPRAZO3(), bpmp820.getTAXAENC());
    addRateDetailIfValid(uniqueRates, rateDetailsList, bpmp820.getPRZMIN(), bpmp820.getTAXAENC());
    addRateDetailIfValid(uniqueRates, rateDetailsList, bpmp820.getPRZMAX(), bpmp820.getTAXAENC());

    rateDetailsList.sort(Comparator.comparingInt(RateTerm::getTerm));

    return rateDetailsList.isEmpty() ? null : rateDetailsList;
  }

  private void addRateDetailIfValid(
      Set<String> uniqueRates, List<RateTerm> rateDetailsList, Integer term, double rate) {

    if (term != null && term > 0) {
      String uniqueKey = term + "-" + rate;
      if (!uniqueRates.contains(uniqueKey)) {
        uniqueRates.add(uniqueKey);
        RateTerm rateDetail = new RateTerm();
        rateDetail.setTerm(term);
        rateDetail.setRate(rate);
        rateDetailsList.add(rateDetail);
      }
    }
  }

  private Integer determineMainTerm(BPMP820 bpmp820) {
    List<Integer> terms =
        Stream.of(
                bpmp820.getPRAZO1(),
                bpmp820.getPRAZO2(),
                bpmp820.getPRAZO3(),
                bpmp820.getPRZMIN(),
                bpmp820.getPRZMAX())
            .filter(Objects::nonNull)
            .toList();

    return terms.isEmpty() ? null : Collections.min(terms);
  }

  private Double safeConvertDouble(double value) {
    return Double.compare(value, 0.0) == 0 ? null : value;
  }
}
