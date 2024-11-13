package com.santander.bp.service;

import com.santander.bp.exception.AppError;
import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.OfferCosmos;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.SubProductCosmos;
import com.santander.bp.model.SubProductDetails;
import com.santander.bp.repository.OffersCosmosDb;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CosmosDbService {

  private static final Logger logger = LoggerFactory.getLogger(CosmosDbService.class);

  @Autowired private OffersCosmosDb offerRepository;

  public List<OffersPricingResponse> getOffers(String cdSegm, String channelCode, String product) {
    logger.info(
        "Searching offers, segment: {}, channel: {}, product: {}", cdSegm, channelCode, product);

    List<OfferCosmos> offers = offerRepository.findOffers(cdSegm, channelCode, product);

    if (offers == null || offers.isEmpty()) {
      logger.warn("Nenhuma oferta encontrada para os critérios fornecidos.");

      throw new RestApiException(AppError.NOT_LIST_ERROR);
    }

    return offers.stream().map(this::mapToOfferResponseDTO).collect(Collectors.toList());
  }

  private OffersPricingResponse mapToOfferResponseDTO(OfferCosmos offer) {
    return OffersPricingResponse.builder()
        .id(offer.getId())
        .product(offer.getProduct())
        .productDescription(offer.getProductDescription())
        .familyCode(offer.getFamilyCode())
        .subProducts(
            offer.getSubProducts().stream()
                .map(this::mapToSubProductDetails)
                .collect(Collectors.toList()))
        .build();
  }

  private SubProductDetails mapToSubProductDetails(SubProductCosmos subProduct) {
    return SubProductDetails.builder()
        .subProduct(subProduct.getNmSubp())
        .minimumApplicationValue(
            subProduct.getVlMiniApli() != null ? subProduct.getVlMiniApli().floatValue() : null)
        .minimumRedeemValue(
            subProduct.getVlMiniResg() != null ? subProduct.getVlMiniResg().floatValue() : null)
        .minimumBalanceValue(
            subProduct.getVlMiniSald() != null ? subProduct.getVlMiniSald().floatValue() : null)
        .progressiveRemunerationIndicator(subProduct.getInRemuPgre())
        .indexerDescription(subProduct.getDsIndx())
        .subCode(subProduct.getCdSubp())
        .graceIndicator(subProduct.getInCare())
        .graceTerm(subProduct.getPzRemuPgre())
        .build();
  }
}
