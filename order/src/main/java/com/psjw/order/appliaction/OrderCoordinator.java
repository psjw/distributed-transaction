package com.psjw.order.appliaction;

import com.psjw.order.appliaction.dto.OrderDto;
import com.psjw.order.appliaction.dto.PlaceOrderCommand;
import com.psjw.order.infrastructure.point.PointApiClient;
import com.psjw.order.infrastructure.point.dto.PointReserveApiRequest;
import com.psjw.order.infrastructure.point.dto.PointReserveCancelApiRequest;
import com.psjw.order.infrastructure.point.dto.PointReserveConfirmApiRequest;
import com.psjw.order.infrastructure.product.ProductApiClient;
import com.psjw.order.infrastructure.product.dto.ProductReserveApiRequest;
import com.psjw.order.infrastructure.product.dto.ProductReserveApiRequest.ReserveItem;
import com.psjw.order.infrastructure.product.dto.ProductReserveApiResponse;
import com.psjw.order.infrastructure.product.dto.ProductReserveCancelApiRequest;
import com.psjw.order.infrastructure.product.dto.ProductReserveConfirmApiRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderCoordinator {
    private final OrderService orderService;
    private final PointApiClient pointApiClient;
    private final ProductApiClient productApiClient;

    public OrderCoordinator(OrderService orderService, PointApiClient pointApiClient,
            ProductApiClient productApiClient) {
        this.orderService = orderService;
        this.pointApiClient = pointApiClient;
        this.productApiClient = productApiClient;
    }

    public void placeOrder(PlaceOrderCommand command) {
        reserve(command.orderId());
        confirm(command.orderId());
    }

    private void reserve(Long orderId) {
        String requestId = orderId.toString();
        orderService.reserve(orderId);


        try{
            OrderDto orderInfo = orderService.getOrder(orderId);

            ProductReserveApiRequest productReserveApiRequest = new ProductReserveApiRequest(
                    requestId,
                    orderInfo.orderItems().stream()
                            .map(
                                    orderItem -> new ReserveItem(
                                            orderItem.productId(),
                                            orderItem.quantity()
                                    )
                            ).toList()
            );
            ProductReserveApiResponse productReserveApiResponse = productApiClient.reserve(productReserveApiRequest);

            PointReserveApiRequest pointReserveApiRequest = new PointReserveApiRequest(
                    requestId,
                    1L,
                    productReserveApiResponse.totalPrice()
            );

            pointApiClient.reservePoint(pointReserveApiRequest);
        } catch (Exception e){
            orderService.cancel(orderId);

            ProductReserveCancelApiRequest productReserveCancelApiRequest = new ProductReserveCancelApiRequest(
                    requestId);

            productApiClient.cancel(productReserveCancelApiRequest);

            PointReserveCancelApiRequest pointReserveCancelApiRequest = new PointReserveCancelApiRequest(
                    requestId);

            pointApiClient.cancelPoint(pointReserveCancelApiRequest);
        }


    }

    public void confirm(Long orderId) {
        String requestId = orderId.toString();
        try{
            ProductReserveConfirmApiRequest productReserveConfirmApiRequest = new ProductReserveConfirmApiRequest(
                    requestId);

            productApiClient.confirm(productReserveConfirmApiRequest);

            PointReserveConfirmApiRequest pointReserveConfirmApiRequest = new PointReserveConfirmApiRequest(
                    requestId);

            pointApiClient.confirmPoint(pointReserveConfirmApiRequest);

            orderService.confirm(orderId);
        } catch (Exception e){
            orderService.pending(orderId);
            throw e;
        }
    }
}
