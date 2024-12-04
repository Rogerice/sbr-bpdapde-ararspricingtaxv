package com.santander.bp.repository;

import com.santander.bp.model.WhitelistEntryDTO;
import java.util.Optional;
import lombok.Generated;

@Generated
public interface WhitelistRepository {

  /**
   * Busca uma entrada na whitelist pelo tipo de documento e número do documento.
   *
   * @param documentType Tipo de documento (CPF ou CNPJ).
   * @param documentNumber Número do documento.
   * @return Uma entrada opcional da whitelist, se encontrada.
   */
  Optional<WhitelistEntryDTO> findByDocument(String documentType, String documentNumber);

  /**
   * Busca uma entrada na whitelist pela agência.
   *
   * @param agency Código da agência.
   * @return Uma entrada opcional da whitelist, se encontrada.
   */
  Optional<WhitelistEntryDTO> findByAgency(String agency);
}
