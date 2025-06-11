package com.santander.bp.app.mapper;

import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.external.BenchmarkIndex;
import com.santander.bp.model.external.InvestmentOrder;
import com.santander.bp.model.external.InvestmentPricingCondition;
import com.santander.bp.model.external.PricingRequest;
import com.santander.bp.model.external.Product;
import com.santander.bp.model.external.PromotionalCode;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Component;

@Component
@Generated
public class PricingRequestMapper {

  public PricingRequest buildFromCosmosOffers(List<OffersPricingResponse> cosmosOffers) {
    if (cosmosOffers == null || cosmosOffers.isEmpty()) {
      return new PricingRequest();
    }

    OffersPricingResponse offer = cosmosOffers.get(0);

    InvestmentOrder investmentOrder = new InvestmentOrder();
    investmentOrder.setTypeCode("STANDARD");

    PromotionalCode promo = new PromotionalCode();
    investmentOrder.setPromotionalCode(promo);

    InvestmentPricingCondition condition = new InvestmentPricingCondition();

    Product product = new Product();
    product.setName(offer.getSubProduct());
    condition.setProduct(product);

    BenchmarkIndex benchmark = new BenchmarkIndex();
    condition.setBenchmarkIndex(benchmark);

    PromotionalCode promoCond = new PromotionalCode();
    condition.setPromotionalCode(promoCond);

    PricingRequest request = new PricingRequest();
    request.setInvestmentOrder(investmentOrder);
    request.setInvestmentPricingConditions(List.of(condition));

    return request;
  }
}
