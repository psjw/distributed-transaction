package com.psjw.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
        status = OrderStatus.CREATED;
    }

    public void complete(){
        status = OrderStatus.COMPLETED;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void request(){
        if(status != OrderStatus.CREATED){
            throw new RuntimeException("잘못된 요청입니다.");
        }
        this.status = OrderStatus.REQUESTED;
    }

    public void fail(){
        if(status != OrderStatus.REQUESTED){
            throw new RuntimeException("잘못된 요청입니다.");
        }
        this.status = OrderStatus.FAILED;
    }

    public Long getId() {
        return id;
    }

    public enum OrderStatus {
        CREATED, REQUESTED, COMPLETED, FAILED
    }
}
