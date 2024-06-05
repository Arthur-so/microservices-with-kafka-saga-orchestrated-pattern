package br.com.microservices.orchestrated.paymentservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    private String id;
    private List<OrderProduct> products;
    private double totalAmount;
    private int totalItems;
    private LocalDateTime createdAt;
    private String transactionId;
}
