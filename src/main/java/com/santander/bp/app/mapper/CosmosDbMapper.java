package com.santander.bp.app.mapper;

import com.santander.bp.model.OfferCosmos;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.SubProductCosmos;
import com.santander.bp.model.SubProductDetails;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CosmosDbMapper {

  public OffersPricingResponse mapToOfferResponseDTO(OfferCosmos offer) {
    return OffersPricingResponse.builder()
        .id(offer.getId())
        .product(offer.getProduct())
        .productDescription(offer.getProductDescription())
        .familyCode(offer.getFamilyCode())
        .subProducts(mapSubProducts(offer.getSubProducts()))
        .build();
  }

  public List<SubProductDetails> mapSubProducts(List<SubProductCosmos> subProducts) {
    return subProducts.stream().map(this::mapToSubProductDetails).collect(Collectors.toList());
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
