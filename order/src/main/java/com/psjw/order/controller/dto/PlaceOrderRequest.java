package com.psjw.order.controller.dto;

import com.psjw.order.appliaction.dto.PlaceOrderCommand;

public record PlaceOrderRequest(
        Long orderId
) {
    public PlaceOrderCommand toCommand() {
        return new PlaceOrderCommand(orderId);
    }
}
