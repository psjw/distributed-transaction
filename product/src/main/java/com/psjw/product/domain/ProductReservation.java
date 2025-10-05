package com.psjw.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_reservations")
public class ProductReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String requestId;

    private Long productId;

    private Long reservedQuantity;

    private Long reservedPrice;

    @Enumerated(EnumType.STRING)
    private ProductReservationStatus status;

    public ProductReservation() {
    }

    public ProductReservation(String requestId, Long productId, Long reservedQuantity,
            Long reservedPrice) {
        this.requestId = requestId;
        this.productId = productId;
        this.reservedQuantity = reservedQuantity;
        this.reservedPrice = reservedPrice;
        this.status = ProductReservationStatus.RESERVED;
    }

    public enum ProductReservationStatus {
        RESERVED,
        CONFIRM,
        CANCELED
    }

    public String getRequestId() {
        return requestId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getReservedQuantity() {
        return reservedQuantity;
    }

    public Long getReservedPrice() {
        return reservedPrice;
    }

    public ProductReservationStatus getStatus() {
        return status;
    }
}
