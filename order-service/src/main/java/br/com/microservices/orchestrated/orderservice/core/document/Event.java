package br.com.microservices.orchestrated.orderservice.core.document;

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
public class Event {
    private String source;
    private String status;
    private List<Event> eventHistory;
    private LocalDateTime createdAt;
    private Order payload;
    private String id;
    private String transactionId;
    private String orderId;
}
