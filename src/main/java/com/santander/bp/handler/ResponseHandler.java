package com.santander.bp.handler;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsError;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.enums.FixedFieldsEnum;
import com.santander.bp.exception.AltairException;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.altair.BPMP82;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseHandler {

	public static boolean shouldRecall(ResponseDto response, OffersPricingRequest request) {
		if (response.getObjeto().getListaFormatos() == null || response.getObjeto().getListaFormatos().isEmpty()) {
			return false;
		}

		Object lastFormat = response.getObjeto().getListaFormatos()
				.get(response.getObjeto().getListaFormatos().size() - 1);

		if (!(lastFormat instanceof PsScreen psScreen)) {
			return false;
		}

		if (!(psScreen.getFormato() instanceof BPMP82 bpmp82Response)) {
			return false;
		}

		String indicatorRecall = bpmp82Response.getINDREA();
		if (FixedFieldsEnum.RECALL_INDICATOR.getValue().equals(indicatorRecall)) {
			request.setIndicatorRecall(bpmp82Response.getINDREA());
			request.setCdRecall(bpmp82Response.getPRODREA());
			request.setSubProductRecall(bpmp82Response.getSUBPREA());
			return true;
		}

		return false;
	}

	public static void handleErrors(ResponseDto response) {
		if (response.getObjeto().getListaErros() == null || response.getObjeto().getListaErros().isEmpty()) {
			return;
		}

		log.warn("Erro retornado pela alta plataforma.");
		for (PsError error : response.getObjeto().getListaErros()) {
			log.warn("Código do Erro: {}, Mensagem do Erro: {}", error.getCodigo(), error.getMensagem());
		}
		throw new AltairException("ERRO_ALTAPLATAFORMA", "Erro na transação", "Detalhes dos erros encontrados");
	}
}
