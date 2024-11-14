package com.santander.bp.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.santander.bp.model.OfferCosmosDTO;
import feign.Param;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OffersCosmosDbRepository extends CosmosRepository<OfferCosmosDTO, String> {

  @Query(
      "SELECT * FROM c WHERE c.cd_segm = @cdSegm AND c.channel_code = @channelCode AND c.product = @product")
  List<OfferCosmosDTO> findOffers(
      @Param("cdSegm") String cdSegm,
      @Param("channelCode") String channelCode,
      @Param("product") String product);
}
