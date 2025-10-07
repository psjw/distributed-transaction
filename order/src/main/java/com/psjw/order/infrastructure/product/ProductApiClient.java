package com.psjw.order.infrastructure.product;

import com.psjw.order.infrastructure.product.dto.ProductReserveApiRequest;
import com.psjw.order.infrastructure.product.dto.ProductReserveApiResponse;
import com.psjw.order.infrastructure.product.dto.ProductReserveCancelApiRequest;
import com.psjw.order.infrastructure.product.dto.ProductReserveConfirmApiRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

public class ProductApiClient {

    private final RestClient restClient;

    public ProductApiClient(RestClient restClient) {
        this.restClient = restClient;
    }


    public ProductReserveApiResponse reserve(ProductReserveApiRequest request) {
        return restClient.post()
                .uri("/products/reserve")
                .body(request)
                .retrieve()
                .body(ProductReserveApiResponse.class);
    }


    public void confirm(ProductReserveConfirmApiRequest request) {
        restClient.post()
                .uri("/products/confirm")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void cancel(ProductReserveCancelApiRequest request) {
        restClient.post()
                .uri("/products/cancel")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
