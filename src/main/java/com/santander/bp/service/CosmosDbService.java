package com.santander.bp.service;

import com.santander.bp.app.mapper.CosmosDbMapper;
import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.repository.OffersCosmosDbRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CosmosDbService {

  private static final Logger logger = LoggerFactory.getLogger(CosmosDbService.class);
  private final OffersCosmosDbRepository offerRepository;
  private final CosmosDbMapper cosmosDbMapper;

  @Autowired
  public CosmosDbService(OffersCosmosDbRepository offerRepository, CosmosDbMapper cosmosDbMapper) {
    this.offerRepository = offerRepository;
    this.cosmosDbMapper = cosmosDbMapper;
  }

  public List<OffersPricingResponse> getOffers(String cdSegm, String channelCode, String product) {
    logOfferSearch(cdSegm, channelCode, product);
    List<OfferCosmosDTO> offers = findOffers(cdSegm, channelCode, product);
    return mapOffersToResponses(offers);
  }

  private void logOfferSearch(String cdSegm, String channelCode, String product) {
    logger.info(
        "Procurando ofertas, segment: {}, channel: {}, product: {}", cdSegm, channelCode, product);
  }

  private List<OfferCosmosDTO> findOffers(String cdSegm, String channelCode, String product) {
    List<OfferCosmosDTO> offers = offerRepository.findOffers(cdSegm, channelCode, product);
    if (offers == null || offers.isEmpty()) {
      logger.warn("Nenhuma oferta encontrada para os critérios fornecidos.");

      return List.of();
    }
    return offers;
  }

  private List<OffersPricingResponse> mapOffersToResponses(List<OfferCosmosDTO> offers) {
    return offers.stream().map(cosmosDbMapper::mapToOfferResponseDTO).collect(Collectors.toList());
  }
}
