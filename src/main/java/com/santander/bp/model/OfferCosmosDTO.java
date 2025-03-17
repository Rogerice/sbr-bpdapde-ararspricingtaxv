package com.santander.bp.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Container(containerName = "simplificacao-bp", autoCreateContainer = false)
public class OfferCosmosDTO {

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("product")
  private String product;

  @JsonProperty("product_description")
  private String productDescription;

  @JsonProperty("family_code")
  private String family;

  @JsonProperty("channel_code")
  private String channelCode;

  @JsonProperty("cd_segm")
  private String cdSegm;

  @JsonProperty("tp_segm")
  private String tpSegm;

  @JsonProperty("sub_products")
  private List<SubProductCosmosDTO> subProducts;
}
