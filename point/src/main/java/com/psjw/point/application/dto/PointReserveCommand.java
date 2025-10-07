package com.psjw.point.application.dto;

public record PointReserveCommand(
        String requestId,
        Long userId,
        Long reserveAmount
) {

}
