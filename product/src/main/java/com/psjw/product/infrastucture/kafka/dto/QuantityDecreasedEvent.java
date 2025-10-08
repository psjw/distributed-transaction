package com.psjw.product.infrastucture.kafka.dto;

public record QuantityDecreasedEvent(
        Long orderId,
        Long totalPrice
) {

}
