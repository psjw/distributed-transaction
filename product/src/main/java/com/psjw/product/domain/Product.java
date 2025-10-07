package com.psjw.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private Long price;

    private Long reservedQuantity;

    @Version
    private Long version;

    public Product() {
    }

    public Product(Long quantity, Long price) {
        this.quantity = quantity;
        this.price = price;
    }

    public void cancel(Long requestQuantity){
        if(this.reservedQuantity < requestQuantity){
            throw new RuntimeException("예약된 수량이 부족ㅎ반디ㅏ.");
        }

        this.reservedQuantity -= requestQuantity;
    }

    public void confirm(Long requestedQuantity){
        if(this.quantity < requestedQuantity){
            throw new RuntimeException("재고가 부족합니다.");
        }

        if(this.reservedQuantity < requestedQuantity){
            throw new RuntimeException("예약된 수량이 부족합니다.");
        }

        this.quantity -= requestedQuantity;
        this.reservedQuantity -= requestedQuantity;
    }

    public Long reserve(Long requestedQuantity) {
        long reservableQuantity = this.quantity - this.reservedQuantity;

        if(reservableQuantity < requestedQuantity) {
            throw new RuntimeException("예약할 수 있는 수량이 부족합니다.");
        }

        reservedQuantity += requestedQuantity;

        return price * requestedQuantity;
    }

    public Long calculatePrice(Long quantity) {
        return price * quantity;
    }

    public void buy(Long quantity) {
        if (this.quantity < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        this.quantity = this.quantity - quantity;
    }
}
