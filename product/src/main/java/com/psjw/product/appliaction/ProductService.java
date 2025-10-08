package com.psjw.product.appliaction;

import com.psjw.product.appliaction.dto.ProductBuyCommand;
import com.psjw.product.appliaction.dto.ProductBuyResult;
import com.psjw.product.domain.Product;
import com.psjw.product.domain.ProductTransactionHistory;
import com.psjw.product.domain.ProductTransactionHistory.TransactionType;
import com.psjw.product.infrastucture.ProductRepository;
import com.psjw.product.infrastucture.ProductTransactionHistoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductTransactionHistoryRepository productTransactionHistoryRepository;

    private final ProductRepository productRepository;

    public ProductService(ProductTransactionHistoryRepository productTransactionHistoryRepository,
            ProductRepository productRepository) {
        this.productTransactionHistoryRepository = productTransactionHistoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductBuyResult buy(ProductBuyCommand command) {
        List<ProductTransactionHistory> histories = productTransactionHistoryRepository.findAllByRequestIdAndTransactionType(
                command.requestId(), TransactionType.PURCHASE);

        if (!histories.isEmpty()) {
            System.out.println("이미 구매한 이력이 있습니다.");

            Long totalPrice = histories.stream()
                    .mapToLong(ProductTransactionHistory::getPrice)
                    .sum();

            return new ProductBuyResult(totalPrice);
        }

        Long totalPrice = 0L;

        for (ProductBuyCommand.ProductInfo productInfo : command.productInfos()) {
            Product product = productRepository.findById(productInfo.productId()).orElseThrow();

            product.buy(productInfo.quantity());
            Long price = product.calculatePrice(productInfo.quantity());
            totalPrice += price;

            productTransactionHistoryRepository.save(new ProductTransactionHistory(
                    command.requestId(),
                    productInfo.productId(),
                    productInfo.quantity(),
                    price,
                    ProductTransactionHistory.TransactionType.PURCHASE
            ));
        }
        return new ProductBuyResult(totalPrice);
    }
}
