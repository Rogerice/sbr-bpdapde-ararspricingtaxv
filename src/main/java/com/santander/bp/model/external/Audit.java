package com.santander.bp.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORREÇÃO: A classe original não continha o campo 'creationApp' que é necessário para montar a
 * requisição do serviço de pricing, conforme a collection do Postman. Adicionamos o campo e as
 * anotações corretas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
  private String creationApp;
}
