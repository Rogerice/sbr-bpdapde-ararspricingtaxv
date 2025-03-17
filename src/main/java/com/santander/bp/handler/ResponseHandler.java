package com.santander.bp.handler;

import java.util.List;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.enums.FixedFieldsEnum;
import com.santander.bp.exception.AltairException;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.altair.BPMP82;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

@Generated
@Slf4j
public final class ResponseHandler {

    private ResponseHandler() {
        throw new UnsupportedOperationException("Esta classe não deve ser instanciada.");
    }

    public static boolean shouldRecall(ResponseDto response, OffersPricingRequest request) {
        if (isInvalidFormatList(response)) {
            return false;
        }

        Object lastFormat = getLastFormat(response);

        boolean shouldRecall = false;
        if (lastFormat instanceof PsScreen psScreen && (psScreen.getFormato() instanceof BPMP82 bpmp82Response)) {
            shouldRecall = handleRecallIndicator(bpmp82Response, request);
        }

        return shouldRecall;
    }

    private static boolean isInvalidFormatList(ResponseDto response) {
        return response.getObjeto().getListaFormatos() == null || response.getObjeto().getListaFormatos().isEmpty();
    }

    private static Object getLastFormat(ResponseDto response) {
        List<PsScreen> formatList = response.getObjeto().getListaFormatos();
        return formatList.get(formatList.size() - 1);
    }

    private static boolean handleRecallIndicator(BPMP82 bpmp82Response, OffersPricingRequest request) {
        boolean hasRecall = FixedFieldsEnum.RECALL_INDICATOR.getValue().equals(bpmp82Response.getINDREA());
        if (hasRecall) {
            request.setIndicatorRecall(bpmp82Response.getINDREA());
            request.setCdRecall(bpmp82Response.getPRODREA());
            request.setSubProductRecall(bpmp82Response.getSUBPREA());
        }
        return hasRecall;
    }

    public static void handleErrors(ResponseDto response) {
        if (isInvalidErrorList(response)) {
            return;
        }

        logErrors(response);
        throw new AltairException("ERRO ALTAIR", "Erro na transação", "Detalhes dos erros encontrados");
    }

    private static boolean isInvalidErrorList(ResponseDto response) {
        return response.getObjeto().getListaErros() == null || response.getObjeto().getListaErros().isEmpty();
    }

    private static void logErrors(ResponseDto response) {
        log.warn("Erro retornado altair ");
        for (PsError error : response.getObjeto().getListaErros()) {
            log.warn("Código do Erro: {}, Mensagem do Erro: {}", error.getCodigo(), error.getMensagem());
        }
    }
}