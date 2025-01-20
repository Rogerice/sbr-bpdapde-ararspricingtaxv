package com.santander.bp.service.whitelist;

import com.santander.bp.model.WhitelistDTO;
import com.santander.bp.repository.WhitelistJpaRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WhitelistService {

  private static final Logger logger = LoggerFactory.getLogger(WhitelistService.class);

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
  @Transactional(readOnly = true)
  public boolean isInWhitelist(String documentType, String documentNumber) {
    try {
      return !whitelistJpaRepository
          .findByDocumentTypeAndDocumentNumber(documentType, documentNumber)
          .isEmpty();
    } catch (Exception e) {
      logger.error(
          "Erro ao verificar se o documento está na whitelist: {}-{}",
          documentType,
          documentNumber,
          e);
      return false;
    }
  }

  /**
   * Verifica se uma agência está na whitelist.
   *
   * @param agencia Código da agência.
   * @return true se a agência estiver na whitelist; false caso contrário.
   */
  @Transactional(readOnly = true)
  public boolean isAgencyInWhitelist(String agencia) {
    try {
      return !whitelistJpaRepository.findByAgencia(agencia).isEmpty();
    } catch (Exception e) {
      logger.error("Erro ao verificar se a agência está na whitelist: {}", agencia, e);
      return false;
    }
  }

  /**
   * Obtém os detalhes da whitelist para um documento específico.
   *
   * @param documentType Tipo de documento (CPF ou CNPJ).
   * @param documentNumber Número do documento.
   * @return Uma lista de objetos WhitelistDTO.
   */
  @Transactional(readOnly = true)
  public List<WhitelistDTO> getWhitelistDetails(String documentType, String documentNumber) {
    try {
      return whitelistJpaRepository
          .findByDocumentTypeAndDocumentNumber(documentType, documentNumber).stream()
          .map(
              entity ->
                  new WhitelistDTO(
                      entity.getId() != null ? entity.getId().longValue() : null,
                      entity.getDocumentType(),
                      entity.getDocumentNumber(),
                      entity.getAgencia()))
          .toList();
    } catch (Exception e) {
      logger.error(
          "Erro ao obter os detalhes da whitelist para documento: {}-{}",
          documentType,
          documentNumber,
          e);
      return List.of();
    }
  }

  /**
   * Obtém os detalhes da whitelist para uma agência específica.
   *
   * @param agencia Código da agência.
   * @return Uma lista de objetos WhitelistDTO.
   */
  @Transactional(readOnly = true)
  public List<WhitelistDTO> getWhitelistDetailsByAgency(String agencia) {
    try {
      return whitelistJpaRepository.findByAgencia(agencia).stream()
          .map(
              entity ->
                  new WhitelistDTO(
                      entity.getId() != null ? entity.getId().longValue() : null,
                      entity.getDocumentType(),
                      entity.getDocumentNumber(),
                      entity.getAgencia()))
          .toList();
    } catch (Exception e) {
      logger.error("Erro ao obter os detalhes da whitelist para a agência: {}", agencia, e);
      return List.of();
    }
  }
}
