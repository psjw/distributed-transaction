package com.psjw.point.infrastructure;

import com.psjw.point.domain.PointTransactionHistory;
import com.psjw.point.domain.PointTransactionHistory.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionHistoryRepository extends JpaRepository<PointTransactionHistory, Long> {
    PointTransactionHistory findByRequestIdAndTransactionType(String requestId, TransactionType transactionType);
}
