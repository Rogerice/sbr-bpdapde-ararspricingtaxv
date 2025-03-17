package com.santander.bp.service;

import br.com.santander.ars.altair.config.ArsenalAltairConfig;
import br.com.santander.ars.altair.core.facade.AltairStrategy;
import com.altec.bsbr.fw.altair.dto.ResponseDto;
import com.santander.bp.app.mapper.OffersMapperBP82;
import com.santander.bp.enums.TransactionEnum;
import com.santander.bp.model.OffersPricingRequest;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.altair.BPMP82;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OffersPricingServiceBP82 extends AltairService {

  private final OffersMapperBP82 offersMapperBP82;

  public OffersPricingServiceBP82(
      ArsenalAltairConfig arsenalAltairConfig,
      AltairStrategy altairStrategy,
      OffersMapperBP82 offersMapperBP82) {
    super(arsenalAltairConfig, altairStrategy);
    this.offersMapperBP82 =
        Objects.requireNonNull(offersMapperBP82, "OffersMapperBP82 não pode ser nulo");
  }

  public List<OffersPricingResponse> processOffers(OffersPricingRequest offersPricingRequest) {
    Objects.requireNonNull(offersPricingRequest, "OffersPricingRequest não pode ser nulo");

    BPMP82 bpmp82 = offersMapperBP82.mapOffersRequest(offersPricingRequest);
    ResponseDto response = sendMessageAltair(TransactionEnum.BP82, bpmp82);

    handleBusinessErrorsIfAny(response);

    return offersMapperBP82.mapOffersResponseList(response);
  }
}
