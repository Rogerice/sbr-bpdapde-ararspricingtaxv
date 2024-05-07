package com.santander.bp.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/** Entity model for AppArsenal. */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppArsenal {

    private long id;

    private String otherInfo;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}