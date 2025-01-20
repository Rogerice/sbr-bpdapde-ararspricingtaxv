package com.santander.bp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "whitelist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WhitelistEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "document_type", nullable = false, length = 10)
  private String documentType;

  @Column(name = "document_number", nullable = false, length = 14)
  private String documentNumber;

  @Column(name = "agencia", nullable = false, length = 10)
  private String agencia;
}
