package com.psjw.monolithic.controller.dto;

import com.psjw.monolithic.order.application.dto.PlaceOrderCommand;

public record PlaceOrderRequest(
        Long orderId
) {

    public PlaceOrderCommand toPlaceORderCommand(){
        return new PlaceOrderCommand(orderId);
    }

}
