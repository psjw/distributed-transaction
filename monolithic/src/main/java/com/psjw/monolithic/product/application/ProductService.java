package com.psjw.monolithic.product.application;

import com.psjw.monolithic.product.domain.Product;
import com.psjw.monolithic.product.infrastucture.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long buy(Long productId, Long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("데이터가 존재하지 않습니다."));

        Long totalPrice = product.calculatePrice(quantity);
        product.buy(quantity);

        productRepository.save(product);

        return totalPrice;
    }
}
