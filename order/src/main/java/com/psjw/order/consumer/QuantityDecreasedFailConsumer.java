package com.psjw.order.consumer;

import com.psjw.order.application.OrderService;
import com.psjw.order.consumer.dto.QuantityDecreasedFailEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QuantityDecreasedFailConsumer {
    private final OrderService orderService;

    public QuantityDecreasedFailConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "quantity-decreased-fail",
            groupId = "quantity-decreased-fail-consumer",
            properties = {
                    "spring.json.value.default.type=com.psjw.order.consumer.dto.QuantityDecreasedFailEvent"
            }
    )
    public void handler(QuantityDecreasedFailEvent event){
        orderService.fail(event.orderId());
    }
}
