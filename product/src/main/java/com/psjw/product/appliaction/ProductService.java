package com.psjw.product.appliaction;

import com.psjw.product.appliaction.dto.ProductBuyCancelResult;
import com.psjw.product.appliaction.dto.ProductBuyCommand;
import com.psjw.product.appliaction.dto.ProductBuyResult;
import com.psjw.product.appliaction.dto.ProductBuyCancelCommand;
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


    @Transactional
    public ProductBuyCancelResult cancel(ProductBuyCancelCommand command) {
        List<ProductTransactionHistory> buyHistories = productTransactionHistoryRepository.findAllByRequestIdAndTransactionType(
                command.requestId(), TransactionType.PURCHASE);

        if (buyHistories.isEmpty()) {
//            throw new RuntimeException("구매이력이 존재하지 않습니다.");
            return new ProductBuyCancelResult(0L);
        }

        List<ProductTransactionHistory> cancelHistories = productTransactionHistoryRepository.findAllByRequestIdAndTransactionType(
                command.requestId(), TransactionType.CANCEL);

        if (!cancelHistories.isEmpty()) {
            System.out.println("이미 취소되었습니다.");
            Long totalPrice = cancelHistories.stream()
                    .mapToLong(ProductTransactionHistory::getPrice)
                    .sum();

            return new ProductBuyCancelResult(totalPrice);
        }

        Long totalPrice = 0L;

        for (ProductTransactionHistory history : buyHistories) {
            Product product = productRepository.findById(history.getProductId()).orElseThrow();

            product.cancel(history.getQuantity());
            totalPrice += history.getPrice();

            productTransactionHistoryRepository.save(new ProductTransactionHistory(
                    command.requestId(),
                    history.getProductId(),
                    history.getQuantity(),
                    history.getPrice(),
                    TransactionType.CANCEL
            ));

        }

        //강제 예외발생
        if(true){
            throw new RuntimeException("강제 예외 발생");
        }

        return new ProductBuyCancelResult(totalPrice);
    }
}
