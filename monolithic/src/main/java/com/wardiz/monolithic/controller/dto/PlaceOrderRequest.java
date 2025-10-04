package com.wardiz.monolithic.controller.dto;

import com.wardiz.monolithic.order.application.dto.PlaceOrderCommand;
import java.util.List;

public record PlaceOrderRequest(
        Long orderId
) {

    public PlaceOrderCommand toPlaceORderCommand(){
        return new PlaceOrderCommand(orderId);
    }

}
