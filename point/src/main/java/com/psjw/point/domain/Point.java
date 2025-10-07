package com.psjw.point.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "points")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long amount;

    private Long reservedAmount;

    @Version
    private Long version;

    public Point() {
    }

    public void reserve(Long reserveAmount) {
        long reservableAmount = this.amount - reserveAmount;

        if(reservableAmount < reserveAmount){
            throw new RuntimeException("금액이 부족합니다.");
        }

        this.reservedAmount += reserveAmount;
    }

    public Point(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }



    public void use(Long amount) {
        if(this.amount < amount){
            throw new RuntimeException("잔액이 부족합니다.");
        }

        this.amount = this.amount - amount;
    }

    public Long getId() {
        return id;
    }
}

