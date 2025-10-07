package com.psjw.order.controller.dto;

import com.psjw.order.appliaction.dto.CreateOrderCommand;
import java.util.List;

public record CreateOrderRequest(
        List<OrderItem> orderItems
) {

    public CreateOrderCommand toCreateOrderCommand() {
        return new CreateOrderCommand(
                orderItems.stream()
                        .map(item -> new CreateOrderCommand.OrderItem(item.productId,
                                item.quantity))
                        .toList()
        );
    }

    public record OrderItem(
            Long productId,
            Long quantity
    ) {

    }
}
