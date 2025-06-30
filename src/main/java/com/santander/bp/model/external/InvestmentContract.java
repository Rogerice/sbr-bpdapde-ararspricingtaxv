package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORREÇÃO: O import para a classe 'Contract' estava apontando para 'feign.Contract', o que
 * causaria um erro de compilação e serialização. Corrigimos para apontar para a classe 'Contract'
 * correta, do próprio pacote 'external'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentContract {
  private Contract contract;
  private Product product;
}
