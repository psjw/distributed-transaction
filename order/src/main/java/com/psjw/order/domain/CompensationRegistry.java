package com.psjw.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "compensation_registries")
@Entity
public class CompensationRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private CompensationRegistryStatus status;

    public CompensationRegistry(Long orderId) {
        this.orderId = orderId;
        this.status = CompensationRegistryStatus.PENDING;
    }

    public enum CompensationRegistryStatus {
        PENDING, COMPLETED
    }

}
