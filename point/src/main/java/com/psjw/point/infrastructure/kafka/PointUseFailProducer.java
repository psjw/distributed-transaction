package com.psjw.point.infrastructure.kafka;


import com.psjw.point.infrastructure.kafka.dto.PointUsedFailEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PointUseFailProducer {
    private final KafkaTemplate<String, PointUsedFailEvent> kafkaTemplate;

    public PointUseFailProducer(KafkaTemplate<String, PointUsedFailEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PointUsedFailEvent event) {
        kafkaTemplate
                .send(
                        "point-use-fail",
                        event.orderId().toString(),
                        event
                );
    }
}
