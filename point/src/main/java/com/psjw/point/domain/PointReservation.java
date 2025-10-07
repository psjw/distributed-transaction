
package com.psjw.point.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "point_reservations")
public class PointReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;

    private Long pointId;

    private Long reservedAmount;

    @Enumerated(EnumType.STRING)
    private PointReservationStatus status;

    public PointReservation() {
    }

    public PointReservation(String requestId, Long pointId, Long reservedAmount) {
        this.requestId = requestId;
        this.pointId = pointId;
        this.reservedAmount = reservedAmount;
        this.status = PointReservationStatus.RESERVED;
    }

    public enum PointReservationStatus {
        RESERVED, CONFIRMED, CANCELED
    }

    public Long getPointId() {
        return pointId;
    }

    public PointReservationStatus getStatus() {
        return status;
    }

    public Long getReservedAmount() {
        return reservedAmount;
    }

    public void confirm(){
        if(this.status == PointReservationStatus.CANCELED){
            throw new RuntimeException("취소된 예약은 확정할 수 없습니다.");
        }

        this.status = PointReservationStatus.CONFIRMED;
    }
}
