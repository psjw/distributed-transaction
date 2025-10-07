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
        this.reservedAmount = 0L;
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


    public void confirm(Long reserveAmount){
        if(this.amount < reserveAmount){
            throw new RuntimeException("퐁딘트가 부족합니다.");
        }

        if(this.reservedAmount < reserveAmount){
            throw new RuntimeException("예약된 금액이 부족합니다.");
        }

        this.amount -= reserveAmount;
        this.reservedAmount -= reserveAmount;
    }

    public void cancel(Long reserveAmount){
        if(this.amount < reserveAmount){
            throw new RuntimeException("예약된 금액이 부족합니다.");
        }

        this.reservedAmount -= reserveAmount;
    }
}

