package com.santander.bp.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.santander.bp.model.OfferCosmos;
import feign.Param;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OffersCosmosDb extends CosmosRepository<OfferCosmos, String> {

  @Query(
      "SELECT * FROM c WHERE c.cd_segm = @cdSegm AND c.channel_code = @channelCode AND c.product = @product")
  List<OfferCosmos> findOffers(
      @Param("cdSegm") String cdSegm,
      @Param("channelCode") String channelCode,
      @Param("product") String product);
}
