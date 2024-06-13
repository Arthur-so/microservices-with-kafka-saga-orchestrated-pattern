package br.com.microservices.orchestrated.paymentservice.core.service;

import br.com.microservices.orchestrated.paymentservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.paymentservice.core.dto.Event;
import br.com.microservices.orchestrated.paymentservice.core.model.Payment;
import br.com.microservices.orchestrated.paymentservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.paymentservice.core.repository.PaymentRepository;
import br.com.microservices.orchestrated.paymentservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentService {
    private static final String CURRENT_SOURCE = "PAYMENT_SERVICE";
    private static final Double REDUCE_SUM = 0.0;
    private final JsonUtil jsonUtil;
    private final KafkaProducer producer;
    private final PaymentRepository paymentRepository;

    public void realizeAndPersistPayment(Event event) {
        try{
            checkForExistingPayment(event);
            createPaymentRecord(event);
        } catch (Exception ex) {
            log.error("Error trying to validate product: ", ex);
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void createPaymentRecord(Event event) {
        var totalItems = calculateTotalItems(event);
        var totalAmount = calculateTotalAmount(event);

        var payment = Payment
                .builder()
                .orderId(event.getPayload().getId())
                .transactionId(event.getTransactionId())
                .totalItems(totalItems)
                .totalAmount(totalAmount)
                .build();
        save(payment);
    }

    private int calculateTotalItems(Event event) {
        return event.getPayload().getProducts()
                .stream()
                .map(orderProduct -> orderProduct.getQuantity())
                .reduce(REDUCE_SUM.intValue(), Integer::sum);
    }

    private double calculateTotalAmount(Event event) {
        return event.getPayload().getProducts()
                .stream()
                .map(orderProduct -> orderProduct.getQuantity() * orderProduct.getProduct().getUnitValue())
                .reduce(REDUCE_SUM, Double::sum);
    }


    private void checkForExistingPayment(Event event) {
        if (paymentRepository.existsByOrderIdAndTransactionId(
                event.getPayload().getId(),
                event.getTransactionId()
        )) {
            throw new ValidationException("There's another transactionId for this payment.");
        }
    }

    private void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
