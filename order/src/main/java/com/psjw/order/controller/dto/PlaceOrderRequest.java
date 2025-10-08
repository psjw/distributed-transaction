package com.psjw.order.controller.dto;

import com.psjw.order.application.dto.PlaceOrderCommand;

public record PlaceOrderRequest(Long orderId) {

    public PlaceOrderCommand toCommand() {
        return new PlaceOrderCommand(orderId);
    }
}
