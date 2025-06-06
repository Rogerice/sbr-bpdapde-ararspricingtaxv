package com.santander.bp.app.mapper;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.external.PricingRequest;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Component;

@Component
@Generated
public class PricingRequestMapper {

  public PricingRequest buildFromCosmosOffers(List<OffersPricingResponse> cosmosOffers) {
    // construção do pricing
    PricingRequest request = new PricingRequest();
    // preenche os campos com base no cosmosOffers, e pricing ...
    return request;
  }
}
