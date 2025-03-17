package com.santander.bp.service;

import com.santander.bp.app.mapper.CosmosDbMapper;
import com.santander.bp.model.OfferCosmosDTO;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.repository.OffersCosmosDbRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CosmosDbService {

  private final OffersCosmosDbRepository offerRepository;
  private final CosmosDbMapper cosmosDbMapper;

  public CosmosDbService(OffersCosmosDbRepository offerRepository, CosmosDbMapper cosmosDbMapper) {
    this.offerRepository = offerRepository;
    this.cosmosDbMapper = cosmosDbMapper;
  }

  public List<OffersPricingResponse> getOffers(String cdSegm, String channelCode, String product) {
    log.info("Procurando ofertas, Segmento={}, Canal={}, Produto={}", cdSegm, channelCode, product);

    List<OfferCosmosDTO> offers = offerRepository.findOffers(cdSegm, channelCode, product);

    if (offers == null || offers.isEmpty()) {
      log.warn("Nenhuma oferta encontrada no CosmosDB.");
      return List.of();
    }

    log.info("Ofertas encontradas no CosmosDB: {}", offers);

    List<OffersPricingResponse> response = cosmosDbMapper.mapOffers(offers);

    log.info("Ofertas mapeadas para Response: {}", response);

    return response;
  }
}
