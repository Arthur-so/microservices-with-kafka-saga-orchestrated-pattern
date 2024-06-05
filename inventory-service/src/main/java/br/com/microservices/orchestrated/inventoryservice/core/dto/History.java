package br.com.microservices.orchestrated.inventoryservice.core.dto;


import br.com.microservices.orchestrated.inventoryservice.core.enums.ESagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class History {
    private String source;
    private ESagaStatus status;
    private String message;
    private LocalDate createdAt;
}