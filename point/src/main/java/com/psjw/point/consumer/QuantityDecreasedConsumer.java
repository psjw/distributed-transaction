package com.psjw.point.consumer;

import com.psjw.point.application.PointService;
import com.psjw.point.application.dto.PointUseCancelCommand;
import com.psjw.point.application.dto.PointUseCommand;
import com.psjw.point.consumer.dto.QuantityDecreasedEvent;
import com.psjw.point.infrastructure.kafka.PointUseFailProducer;
import com.psjw.point.infrastructure.kafka.PointUsedProducer;
import com.psjw.point.infrastructure.kafka.dto.PointUsedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QuantityDecreasedConsumer {
    private final PointService pointService;

    private final PointUsedProducer pointUsedProducer;

    private final PointUseFailProducer pointUseFailProducer;
    public QuantityDecreasedConsumer(PointService pointService, PointUsedProducer pointUsedProducer,
            PointUseFailProducer pointUseFailProducer) {
        this.pointService = pointService;
        this.pointUsedProducer = pointUsedProducer;
        this.pointUseFailProducer = pointUseFailProducer;
    }

    @KafkaListener(
            topics = "quantity-decreased",
            groupId = "quantity-decreased-consumer",
            properties = {
                    "spring.json.value.default.type=com.psjw.point.consumer.QuantityDecreasedEvent"
            }

    )
    public void handle(QuantityDecreasedEvent event) {
        String requestId = event.orderId().toString();
        try{
            pointService.use(new PointUseCommand(requestId, 1L, event.totalPrice()));

            pointUsedProducer.send(new PointUsedEvent(event.orderId()));
        }catch (Exception e){
            pointService.cancel(new PointUseCancelCommand(requestId));

            pointUsedProducer.send(new PointUsedEvent(event.orderId()));
        }

    }
}
