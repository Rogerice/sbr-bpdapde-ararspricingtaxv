package com.santander.bp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.altec.bsbr.fw.altair.dto.ResponseDto;
// Importe a exceção correta do seu projeto, se for diferente de RuntimeException
// import com.altec.bsbr.fw.exception.BusinessException; 
import com.altec.bsbr.fw.ps.parser.object.PsObjectReturn;
import com.altec.bsbr.fw.ps.parser.object.PsScreen;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.enums.TransactionEnum;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairStrategy;

/**
 * Testes unitários para a classe OffersPricingServiceBP82.
 */
@ExtendWith(MockitoExtension.class)
class OffersPricingServiceBP82Test {

	@Mock
	private ArsenalAltairConfig arsenalAltairConfig;
	@Mock
	private AltairStrategy altairStrategy;
	@Mock
	private OffersMapperBP82 offersMapperBP82;

	@Spy
	@InjectMocks
	private OffersPricingServiceBP82 offersPricingServiceBP82;

	private OffersPricingRequest defaultRequest;

	@BeforeEach
	void setUp() {
		defaultRequest = new OffersPricingRequest();
	}

	@Test
	@DisplayName("Deve lançar NullPointerException quando o OffersMapperBP82 for nulo")
	void constructor_shouldThrowNPE_whenMapperIsNull() {
		assertThrows(NullPointerException.class,
				() -> new OffersPricingServiceBP82(arsenalAltairConfig, altairStrategy, null),
				"O construtor deveria lançar NullPointerException se o mapper for nulo.");
	}

	@Test
	@DisplayName("Deve processar com sucesso uma única chamada quando não há indicação de rechamada")
	void processOffers_shouldSucceedWithSingleCall_whenNoRecallIsNeeded() {
		BPMP82 responseFormat = createResponseFormat("N", null, null);
		ResponseDto altairResponse = createAltairResponse(responseFormat);
		List<OffersPricingResponse> expectedResponseList = Collections.singletonList(new OffersPricingResponse());

		when(offersMapperBP82.mapOffersRequest(any(OffersPricingRequest.class))).thenReturn(new BPMP82());
		when(offersMapperBP82.mapOffersResponseList(altairResponse)).thenReturn(expectedResponseList);

		doReturn(altairResponse).when(offersPricingServiceBP82).sendMessageAltair(any(TransactionEnum.class), any(BPMP82.class));
		doNothing().when(offersPricingServiceBP82).handleBusinessErrorsIfAny(any(ResponseDto.class));
		
		List<OffersPricingResponse> actualResponse = offersPricingServiceBP82.processOffers(defaultRequest);

		assertNotNull(actualResponse);
		assertEquals(1, actualResponse.size());
		verify(offersPricingServiceBP82, times(1)).sendMessageAltair(any(TransactionEnum.class), any(BPMP82.class));
		verify(offersMapperBP82, times(1)).mapOffersRequest(any(OffersPricingRequest.class));
		verify(offersMapperBP82, times(1)).mapOffersResponseList(altairResponse);
	}

	@Test
	@DisplayName("Deve realizar uma rechamada e acumular os resultados")
	void processOffers_shouldPerformRecallAndAccumulateResults() {
		BPMP82 firstResponseFormat = createResponseFormat("S", "PROD1", "SUB1");
		BPMP82 secondResponseFormat = createResponseFormat("N", null, null);
		ResponseDto firstAltairResponse = createAltairResponse(firstResponseFormat);
		ResponseDto secondAltairResponse = createAltairResponse(secondResponseFormat);
		OffersPricingResponse firstOffer = new OffersPricingResponse();
		OffersPricingResponse secondOffer = new OffersPricingResponse();
		
		when(offersMapperBP82.mapOffersRequest(any(OffersPricingRequest.class))).thenReturn(new BPMP82());
		when(offersMapperBP82.mapOffersResponseList(any(ResponseDto.class)))
				.thenReturn(Collections.singletonList(firstOffer))
				.thenReturn(Collections.singletonList(secondOffer));
		
		doReturn(firstAltairResponse)
			.doReturn(secondAltairResponse)
			.when(offersPricingServiceBP82).sendMessageAltair(any(TransactionEnum.class), any(BPMP82.class));
		doNothing().when(offersPricingServiceBP82).handleBusinessErrorsIfAny(any(ResponseDto.class));

		List<OffersPricingResponse> actualResponse = offersPricingServiceBP82.processOffers(defaultRequest);

		assertNotNull(actualResponse);
		assertEquals(2, actualResponse.size());
		verify(offersPricingServiceBP82, times(2)).sendMessageAltair(any(TransactionEnum.class), any(BPMP82.class));
		
		ArgumentCaptor<OffersPricingRequest> requestCaptor = ArgumentCaptor.forClass(OffersPricingRequest.class);
		verify(offersMapperBP82, times(2)).mapOffersRequest(requestCaptor.capture());
		
		OffersPricingRequest finalRequestUsed = requestCaptor.getValue();
		assertEquals("S", finalRequestUsed.getIndicatorRecall());
		assertEquals("PROD1", finalRequestUsed.getCdRecall());
		assertEquals("SUB1", finalRequestUsed.getSubProductRecall());
	}
	
	@Test
	@DisplayName("Deve parar a rechamada se a lista de formatos da resposta for nula")
	void processOffers_shouldStopRecall_whenFormatListIsNull() {
		ResponseDto malformedResponse = new ResponseDto();
		PsObjectReturn psObjectReturn = new PsObjectReturn();
		psObjectReturn.setListaFormatos(null);
		malformedResponse.setObjeto(psObjectReturn);
		
		when(offersMapperBP82.mapOffersRequest(any())).thenReturn(new BPMP82());
		doReturn(malformedResponse).when(offersPricingServiceBP82).sendMessageAltair(any(), any());
		doNothing().when(offersPricingServiceBP82).handleBusinessErrorsIfAny(any());

		offersPricingServiceBP82.processOffers(defaultRequest);

		verify(offersPricingServiceBP82, times(1)).sendMessageAltair(any(), any());
	}

	@Test
	@DisplayName("Deve parar a rechamada se o objeto de resposta for nulo")
	void processOffers_shouldStopRecall_whenResponseObjectIsNull() {
		ResponseDto responseWithNullObject = new ResponseDto();
		responseWithNullObject.setObjeto(null);

		when(offersMapperBP82.mapOffersRequest(any())).thenReturn(new BPMP82());
		doReturn(responseWithNullObject).when(offersPricingServiceBP82).sendMessageAltair(any(), any());
		doNothing().when(offersPricingServiceBP82).handleBusinessErrorsIfAny(any());

		offersPricingServiceBP82.processOffers(defaultRequest);

		verify(offersPricingServiceBP82, times(1)).sendMessageAltair(any(), any());
	}
	
	@Test
	@DisplayName("Deve parar a rechamada se o conteúdo do último formato não for do tipo esperado")
	void processOffers_shouldStopRecall_whenLastFormatContentIsNotCorrectType() {
		ResponseDto responseWithWrongFormatType = new ResponseDto();
		PsObjectReturn psObjectReturn = new PsObjectReturn();
		
		// Criamos uma tela (PsScreen), mas colocamos um objeto inesperado dentro dela
		PsScreen screenWithWrongContent = new PsScreen();
		screenWithWrongContent.setFormato(new Object()); // <--- Simula o erro
		
		List<PsScreen> formats = new ArrayList<>(); // <--- CORREÇÃO DE TIPO
		formats.add(screenWithWrongContent);
		
		psObjectReturn.setListaFormatos(formats); // <--- CORREÇÃO DE TIPO
		responseWithWrongFormatType.setObjeto(psObjectReturn);

		when(offersMapperBP82.mapOffersRequest(any())).thenReturn(new BPMP82());
		doReturn(responseWithWrongFormatType).when(offersPricingServiceBP82).sendMessageAltair(any(), any());
		doNothing().when(offersPricingServiceBP82).handleBusinessErrorsIfAny(any());
		
		offersPricingServiceBP82.processOffers(defaultRequest);

		verify(offersPricingServiceBP82, times(1)).sendMessageAltair(any(), any());
	}

	@Test
	@DisplayName("Deve propagar uma exceção em caso de erro de negócio")
	void processOffers_shouldPropagateException_onBusinessError() {
		// ATENÇÃO: Verifique no seu projeto qual é a exceção correta lançada por handleBusinessErrorsIfAny.
		// Estou usando RuntimeException como um placeholder para o código compilar.
		// Troque 'RuntimeException' pela exceção correta (ex: BusinessException, AltecException, etc.)
		Class<? extends RuntimeException> expectedException = RuntimeException.class;
		
		when(offersMapperBP82.mapOffersRequest(any(OffersPricingRequest.class))).thenReturn(new BPMP82());
		
		doReturn(new ResponseDto()).when(offersPricingServiceBP82).sendMessageAltair(any(), any());
		doThrow(expectedException).when(offersPricingServiceBP82).handleBusinessErrorsIfAny(any(ResponseDto.class));

		assertThrows(expectedException, () -> {
			offersPricingServiceBP82.processOffers(defaultRequest);
		});
		
		verify(offersPricingServiceBP82, times(1)).sendMessageAltair(any(), any());
		verify(offersPricingServiceBP82, times(1)).handleBusinessErrorsIfAny(any(ResponseDto.class));
	}

	private BPMP82 createResponseFormat(String indRea, String prodRea, String subPrea) {
		BPMP82 responseFormat = new BPMP82();
		responseFormat.setINDREA(indRea);
		responseFormat.setPRODREA(prodRea);
		responseFormat.setSUBPREA(subPrea);
		return responseFormat;
	}

	private ResponseDto createAltairResponse(BPMP82 bpmp82) {
		ResponseDto responseDto = new ResponseDto();
		PsObjectReturn psObjectReturn = new PsObjectReturn();
		PsScreen psScreen = new PsScreen();
		psScreen.setFormato(bpmp82);
		
		List<PsScreen> formats = new ArrayList<>(); // <--- CORREÇÃO DE TIPO
		formats.add(psScreen);
		psObjectReturn.setListaFormatos(formats); // <--- CORREÇÃO DE TIPO
		responseDto.setObjeto(psObjectReturn);
		
		return responseDto;
	}
}
