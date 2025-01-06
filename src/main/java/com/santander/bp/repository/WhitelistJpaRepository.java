package com.santander.bp.repository;

import com.santander.bp.entity.WhitelistEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhitelistJpaRepository extends JpaRepository<WhitelistEntity, Long> {

  List<WhitelistEntity> findByDocumentTypeAndDocumentNumber(
      String documentType, String documentNumber);

  List<WhitelistEntity> findByAgencia(String agencia);
}
