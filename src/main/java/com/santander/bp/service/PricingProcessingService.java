package com.santander.bp.service;

import com.santander.bp.app.mapper.PricingMapper;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.PricingRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PricingProcessingService {

  private final PricingMapper pricingMapper;

  public PricingProcessingService(PricingMapper pricingMapper) {
    this.pricingMapper = pricingMapper;
  }

  public CompletableFuture<List<OffersPricingResponse>> processPricing(PricingRequest request) {
    return CompletableFuture.supplyAsync(
        () -> {
          log.info("Processando precificação para o request: {}", request);

          List<OffersPricingResponse> response = pricingMapper.mapToOffersPricingResponse(request);

          log.info("Resposta do Pricing gerada: {}", response);
          return response;
        });
  }
}
