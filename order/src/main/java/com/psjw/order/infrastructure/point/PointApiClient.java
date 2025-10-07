package com.psjw.order.infrastructure.point;

import com.psjw.order.infrastructure.point.dto.PointReserveApiRequest;
import com.psjw.order.infrastructure.point.dto.PointReserveCancelApiRequest;
import com.psjw.order.infrastructure.point.dto.PointReserveConfirmApiRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


public class PointApiClient {
    private final RestClient restClient;

    public PointApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void reservePoint(PointReserveApiRequest request){
        restClient.post()
                .uri("/point/reserve")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }


    public void confirmPoint(PointReserveConfirmApiRequest request){
        restClient.post()
                .uri("/point/confirm")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void cancelPoint(PointReserveCancelApiRequest request){
        restClient.post()
                .uri("/point/cancel")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
