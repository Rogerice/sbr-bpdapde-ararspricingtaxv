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
      return new PricingRequest(null, List.of());
    }

    OffersPricingResponse offer = cosmosOffers.get(0);

    PromotionalCode promotionalCodeForOrder = new PromotionalCode(null);
    PromotionalCode promotionalCodeForCondition = new PromotionalCode(null);

    BenchmarkIndex benchmark = new BenchmarkIndex(null);

    Product product = new Product(offer.getSubProduct(), null, null);

    InvestmentPricingCondition condition =
        new InvestmentPricingCondition(product, benchmark, promotionalCodeForCondition);

    InvestmentOrder investmentOrder =
        new InvestmentOrder(
            null, // investmentTradeChannel
            null, // netAmount
            null, // investmentContract
            "STANDARD", // typeCode
            promotionalCodeForOrder, // promotionalCode
            null // audit
            );

    return new PricingRequest(investmentOrder, List.of(condition));
  }
}
