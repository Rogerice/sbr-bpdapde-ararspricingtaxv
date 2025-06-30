package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORREÇÃO: A classe original não possuía os campos corretos para corresponder à estrutura esperada
 * pelo serviço de pricing (conforme a collection do Postman). Os campos foram ajustados para
 * incluir 'price', 'gracePeriod' e 'totalTerm', e o 'promotionalCode' foi removido desta classe,
 * pois ele não pertence a este objeto na requisição.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPricingCondition {
  private Product product;
  private BenchmarkIndex benchmarkIndex;
  private Price price;
  private Term gracePeriod;
  private Term totalTerm;
}
