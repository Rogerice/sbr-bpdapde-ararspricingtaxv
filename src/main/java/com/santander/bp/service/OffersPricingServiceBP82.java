package com.santander.bp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.enums.TransactionEnum;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OffersPricingServiceBP82 extends AltairService {

	private final OffersMapperBP82 offersMapperBP82;

	public OffersPricingServiceBP82(ArsenalAltairConfig arsenalAltairConfig, AltairStrategy altairStrategy,
			OffersMapperBP82 offersMapperBP82) {
		super(arsenalAltairConfig, altairStrategy);
		this.offersMapperBP82 = Objects.requireNonNull(offersMapperBP82, "OffersMapperBP82 não pode ser nulo");
	}

	public List<OffersPricingResponse> processOffers(OffersPricingRequest offersPricingRequest) {
		List<OffersPricingResponse> acumulado = new ArrayList<>();
		boolean precisaRecall;
		int count = 1;

		do {
			log.info("[BP82] Rechamada #{} - Request: {}", count, offersPricingRequest);

			BPMP82 bpmp82 = offersMapperBP82.mapOffersRequest(offersPricingRequest);
			ResponseDto response = sendMessageAltair(TransactionEnum.BP82, bpmp82);
			handleBusinessErrorsIfAny(response);

			log.info("[BP82] Rechamada #{} - Response: {}", count, response);

			List<OffersPricingResponse> respostaAtual = offersMapperBP82.mapOffersResponseList(response);
			acumulado.addAll(respostaAtual);

			Optional<BPMP82> optRecallData = extractLastBpmp82From(response);

			if (optRecallData.isPresent() && "S".equalsIgnoreCase(optRecallData.get().getINDREA())) {
				precisaRecall = true;
				offersPricingRequest.setIndicatorRecall(optRecallData.get().getINDREA());
				offersPricingRequest.setCdRecall(optRecallData.get().getPRODREA());
				offersPricingRequest.setSubProductRecall(optRecallData.get().getSUBPREA());

				log.info("[BP82] Rechamada #{} - INDREA={}, PRODREA={}, SUBPREA={}",
						count, optRecallData.get().getINDREA(), optRecallData.get().getPRODREA(), optRecallData.get().getSUBPREA());
			} else {
				precisaRecall = false;
			}

			count++;
		} while (precisaRecall);

		log.info("[BP82] Total de rechamadas realizadas: {}", count - 1);
		return acumulado;
	}

	private Optional<BPMP82> extractLastBpmp82From(ResponseDto response) {
		if (response.getObjeto() == null || response.getObjeto().getListaFormatos() == null
				|| response.getObjeto().getListaFormatos().isEmpty()) {
			return Optional.empty();
		}

		Object lastFormat = response.getObjeto().getListaFormatos()
				.get(response.getObjeto().getListaFormatos().size() - 1);

		if (lastFormat instanceof PsScreen psScreen && psScreen.getFormato() instanceof BPMP82 lastBpmp82) {
			return Optional.of(lastBpmp82);
		}

		return Optional.empty();
	}
}
