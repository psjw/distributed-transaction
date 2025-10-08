package com.psjw.order.infrastructure.kafka;

import com.psjw.order.infrastructure.kafka.dto.OrderPlacedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> orderKafkaTemplate;

    public OrderPlacedProducer(KafkaTemplate<String, OrderPlacedEvent> orderKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
    }

    public void send(OrderPlacedEvent event) {
        orderKafkaTemplate.send(
                "order-placed",
                event.orderId().toString(),
                event
        );
    }
}
