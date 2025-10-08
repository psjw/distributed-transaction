package com.psjw.product.infrastucture;

import com.psjw.product.domain.ProductTransactionHistory;
import com.psjw.product.domain.ProductTransactionHistory.TransactionType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTransactionHistoryRepository extends
        JpaRepository<ProductTransactionHistory, Long> {
    List<ProductTransactionHistory> findAllByRequestIdAndTransactionType(String requestId, TransactionType transactionType);
}
