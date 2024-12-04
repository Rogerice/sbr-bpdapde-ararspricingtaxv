package com.santander.bp.app.mapper;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.enums.FixedFieldsEnum;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.RateDetails;
import com.santander.bp.model.SubProductDetails;
import com.santander.bp.model.altair.BPMP82;
import com.santander.bp.model.altair.BPMP820;
import java.util.ArrayList;
import java.util.List;
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

  private OffersPricingResponse construirOffersPricingResponse(BPMP820 bpmp820) {
    OffersPricingResponse response = new OffersPricingResponse();
    response.setProduct(bpmp820.getPRODUTO());
    response.setProductDescription(bpmp820.getDESSUBP());
    response.setFamilyCode(bpmp820.getFAMILIA());

    List<SubProductDetails> subProducts = new ArrayList<>();
    SubProductDetails subProduct = construirSubProduto(bpmp820);
    subProducts.add(subProduct);
    response.setSubProducts(subProducts);

    return response;
  }

  private SubProductDetails construirSubProduto(BPMP820 bpmp820) {
    SubProductDetails subProduct = new SubProductDetails();
    subProduct.setSubProduct(bpmp820.getSUBPROD());
    subProduct.setMinimumApplicationValue((float) bpmp820.getVLRMINA());
    subProduct.setMinimumRedeemValue((float) bpmp820.getVLRMINR());
    subProduct.setMinimumBalanceValue((float) bpmp820.getSLDMIN());
    subProduct.setProgressiveRemunerationIndicator(bpmp820.getINDPRG());
    subProduct.setIndexerDescription(bpmp820.getINDPRG());
    subProduct.setGraceIndicator(bpmp820.getINCARE());
    subProduct.setGraceTerm(bpmp820.getPRZCARE().toString());
    subProduct.setOtherTerm(bpmp820.getOTRPRAZ());
    subProduct.setRedeemIndicator(bpmp820.getINRESG());
    subProduct.setFutureSchedule(bpmp820.getAGNFUTU());
    subProduct.setOnlineHour(bpmp820.getHRONLIN());

    List<RateDetails> rateDetailsList = new ArrayList<>();
    RateDetails rateDetails = new RateDetails();
    rateDetails.setTaxRate(String.valueOf(bpmp820.getTAXAENC()));
    rateDetails.setTaxRateDescription(bpmp820.getDESTAXA());
    rateDetails.setReceivedTaxRate(String.valueOf(bpmp820.getTAXAREC()));
    rateDetailsList.add(rateDetails);

    subProduct.setRateDetails(rateDetailsList);
    return subProduct;
  }
}
