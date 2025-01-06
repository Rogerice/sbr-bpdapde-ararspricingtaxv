package com.santander.bp.service.whitelist;

import com.santander.bp.entity.WhitelistEntity;
import com.santander.bp.model.WhitelistDTO;
import com.santander.bp.repository.WhitelistJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WhitelistService {

  private final WhitelistJpaRepository whitelistJpaRepository;

  public WhitelistService(WhitelistJpaRepository whitelistJpaRepository) {
    this.whitelistJpaRepository = whitelistJpaRepository;
  }

  /**
   * Verifica se um documento está na whitelist.
   *
   * @param documentType Tipo de documento (CPF ou CNPJ).
   * @param documentNumber Número do documento.
   * @return true se houver pelo menos um registro na whitelist para o documento; false caso
   *     contrário.
   */
  public boolean isInWhitelist(String documentType, String documentNumber) {
    List<WhitelistEntity> results =
        whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    return !results.isEmpty(); // Retorna true se houver ao menos um resultado
  }

  /**
   * Verifica se uma agência está na whitelist.
   *
   * @param agencia Código da agência.
   * @return true se a agência estiver na whitelist; false caso contrário.
   */
  public boolean isAgencyInWhitelist(String agencia) {
    List<WhitelistEntity> results = whitelistJpaRepository.findByAgencia(agencia);
    return !results.isEmpty(); // Retorna true se houver ao menos um resultado
  }

  /**
   * Obtém os detalhes da whitelist para um documento específico.
   *
   * @param documentType Tipo de documento (CPF ou CNPJ).
   * @param documentNumber Número do documento.
   * @return Uma lista de objetos WhitelistDTO.
   */
  public List<WhitelistDTO> getWhitelistDetails(String documentType, String documentNumber) {
    List<WhitelistEntity> entities =
        whitelistJpaRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    return entities.stream()
        .map(entity -> new WhitelistDTO(entity.getDocumentNumber(), entity.getAgencia()))
        .collect(Collectors.toList());
  }

  /**
   * Obtém os detalhes da whitelist para uma agência específica.
   *
   * @param agencia Código da agência.
   * @return Uma lista de objetos WhitelistDTO.
   */
  public List<WhitelistDTO> getWhitelistDetailsByAgency(String agencia) {
    List<WhitelistEntity> entities = whitelistJpaRepository.findByAgencia(agencia);
    return entities.stream()
        .map(entity -> new WhitelistDTO(entity.getDocumentNumber(), entity.getAgencia()))
        .collect(Collectors.toList());
  }
}
