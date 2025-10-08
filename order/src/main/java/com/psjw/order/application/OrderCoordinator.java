package com.psjw.order.application;

import com.psjw.order.application.dto.OrderDto;
import com.psjw.order.application.dto.PlaceOrderCommand;
import com.psjw.order.infrastructure.point.PointApiClient;
import com.psjw.order.infrastructure.point.PointUseApiRequest;
import com.psjw.order.infrastructure.point.PointUseCancelApiRequest;
import com.psjw.order.infrastructure.product.ProductApiClient;
import com.psjw.order.infrastructure.product.ProductBuyApiRequest;
import com.psjw.order.infrastructure.product.ProductBuyApiRequest.ProductInfo;
import com.psjw.order.infrastructure.product.ProductBuyApiResponse;
import com.psjw.order.infrastructure.product.ProductBuyCancelApiRequest;
import com.psjw.order.infrastructure.product.ProductBuyCancelApiResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderCoordinator {
    private final OrderService orderService;

    private final ProductApiClient productApiClient;

    private final PointApiClient pointApiClient;

    public OrderCoordinator(OrderService orderService, ProductApiClient productApiClient,
            PointApiClient pointApiClient) {
        this.orderService = orderService;
        this.productApiClient = productApiClient;
        this.pointApiClient = pointApiClient;
    }

    public void placeOrder(PlaceOrderCommand command){
        orderService.request(command.orderId());
        OrderDto orderDto = orderService.getOrder(command.orderId());
        try{

            ProductBuyApiRequest productBuyApiRequest = new ProductBuyApiRequest(
                    command.orderId().toString(),
                    orderDto.orderItems().stream()
                            .map(item -> new ProductInfo(
                                    item.productId(),
                                    item.quantity()
                            )).toList()
            );

            ProductBuyApiResponse buyApiResponse = productApiClient.buy(productBuyApiRequest);

            PointUseApiRequest pointUseApiRequest = new PointUseApiRequest(
                    command.orderId().toString(),
                    1L,
                    buyApiResponse.totalPrice()
            );

            pointApiClient.use(pointUseApiRequest);

            orderService.complete(command.orderId());
        }catch (Exception e){
            ProductBuyCancelApiRequest productBuyCancelApiRequest = new ProductBuyCancelApiRequest(
                    command.orderId().toString());

            ProductBuyCancelApiResponse productBuyCancelApiResponse = productApiClient.cancel(
                    productBuyCancelApiRequest);

            if(productBuyCancelApiResponse.totalPrice() > 0){
                PointUseCancelApiRequest pointUseCancelApiRequest = new PointUseCancelApiRequest(
                        command.orderId().toString());

                pointApiClient.cancel(pointUseCancelApiRequest);
            }

            orderService.fail(command.orderId());
        }
    }
}
