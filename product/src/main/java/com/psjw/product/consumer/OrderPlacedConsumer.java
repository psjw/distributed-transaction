package com.psjw.product.consumer;

import com.psjw.product.appliaction.ProductService;
import com.psjw.product.appliaction.dto.ProductBuyCancelCommand;
import com.psjw.product.appliaction.dto.ProductBuyCommand;
import com.psjw.product.appliaction.dto.ProductBuyCommand.ProductInfo;
import com.psjw.product.appliaction.dto.ProductBuyResult;
import com.psjw.product.consumer.dto.OrderPlacedEvent;
import com.psjw.product.infrastucture.kafka.QuantityDecreasedFailProducer;
import com.psjw.product.infrastucture.kafka.QuantityDecreasedProducer;
import com.psjw.product.infrastucture.kafka.dto.QuantityDecreasedEvent;
import com.psjw.product.infrastucture.kafka.dto.QuantityDecreasedFailEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedConsumer {
    private final ProductService productService;
    private final QuantityDecreasedProducer quantityDecreasedProducer;
    private final QuantityDecreasedFailProducer quantityDecreasedFailProducer;

    public OrderPlacedConsumer(ProductService productService,
            QuantityDecreasedProducer quantityDecreasedProducer,
            QuantityDecreasedFailProducer quantityDecreasedFailProducer) {
        this.productService = productService;
        this.quantityDecreasedProducer = quantityDecreasedProducer;
        this.quantityDecreasedFailProducer = quantityDecreasedFailProducer;
    }

    @KafkaListener(
            topics = "order-placed",
            groupId = "order-placed-consumer",
            properties = {
                "spring.json.value.default.type=com.psjw.product.consumer.dto.OrderPlacedEvent"
            }
    )
    public void handle(OrderPlacedEvent event){
        String requestId = event.orderId().toString();

        try {
            ProductBuyResult result = productService.buy(
                    new ProductBuyCommand(
                            requestId,
                            event.productInfos()
                                    .stream()
                                    .map(item -> new ProductInfo(item.productId(),
                                            item.quantity()))
                                    .toList()
                    )
            );

            quantityDecreasedProducer.send(
                    new QuantityDecreasedEvent(
                            event.orderId(),
                            result.totalPrice()
                    )
            );

        } catch (Exception ex) {
            productService.cancel(new ProductBuyCancelCommand(requestId));

            quantityDecreasedFailProducer.send(
                    new QuantityDecreasedFailEvent(event.orderId())
            );
        }

    }
}
