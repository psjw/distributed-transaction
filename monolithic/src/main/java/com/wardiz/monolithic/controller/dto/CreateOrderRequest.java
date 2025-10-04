package com.wardiz.monolithic.controller.dto;

import com.wardiz.monolithic.order.application.dto.CreateOrderCommand;
import com.wardiz.monolithic.order.application.dto.CreateOrderCommand.OrderItem;
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
