package com.psjw.point.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "point_transaction_history")
@Entity
public class PointTransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;

    private Long pointId;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public PointTransactionHistory() {
    }

    public PointTransactionHistory(String requestId, Long pointId, Long amount,
            TransactionType transactionType) {
        this.requestId = requestId;
        this.pointId = pointId;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public enum TransactionType {
        USE, CANCEL
    }

}
