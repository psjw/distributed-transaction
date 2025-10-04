package com.wardiz.monolithic.controller.dto;

import com.wardiz.monolithic.order.application.dto.PlaceOrderCommand;
import java.util.List;

public record PlaceOrderRequest(
        List<OrderItem> orderItems
) {

    public PlaceOrderCommand toPlaceORderCommand(){
        return new PlaceOrderCommand(
                orderItems.stream()
                        .map(item -> new PlaceOrderCommand.OrderItem(
                                item.productId,
                                item.quantity
                        ))
                        .toList()
        );
    }

    public record OrderItem(
            Long productId,
            Long quantity
    ){

    }

}
