package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORREÇÃO: O import para a classe 'Document' estava incorreto na versão original. Ajustamos para
 * usar a classe 'Document' do próprio pacote 'external', o que é essencial para a correta
 * serialização do JSON.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
  private Document document;
}
