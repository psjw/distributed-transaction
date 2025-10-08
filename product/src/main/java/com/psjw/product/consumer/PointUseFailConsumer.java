package com.psjw.product.consumer;

import com.psjw.product.appliaction.ProductService;
import com.psjw.product.appliaction.dto.ProductBuyCancelCommand;
import com.psjw.product.consumer.dto.PointUsedFailEvent;
import com.psjw.product.infrastucture.kafka.QuantityDecreasedFailProducer;
import com.psjw.product.infrastucture.kafka.dto.QuantityDecreasedFailEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PointUseFailConsumer {

    private final ProductService productService;
    private final QuantityDecreasedFailProducer quantityDecreasedFailProducer;

    public PointUseFailConsumer(ProductService productService,
            QuantityDecreasedFailProducer quantityDecreasedFailProducer) {
        this.productService = productService;
        this.quantityDecreasedFailProducer = quantityDecreasedFailProducer;
    }

    @KafkaListener(
            topics = "point-use-fail",
            groupId = "point-use-fail-consumer",
            properties = {
                    "spring.json.value.default.type=com.psjw.product.consumer.dto.PointUsedFailEvent"
            }
    )
    public void handle(PointUsedFailEvent event) {
        productService.cancel(new ProductBuyCancelCommand(event.orderId().toString()));
        quantityDecreasedFailProducer.send(new QuantityDecreasedFailEvent(event.orderId()));
    }
}
