package com.wardiz.monolithic.init;

import com.wardiz.monolithic.order.infrastructure.OrderRepository;
import com.wardiz.monolithic.point.domain.Point;
import com.wardiz.monolithic.point.infrastructure.PointRepository;
import com.wardiz.monolithic.product.domain.Product;
import com.wardiz.monolithic.product.infrastucture.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TestDataCreator {
    private final PointRepository pointRepository;

    private final ProductRepository productRepository;

    public TestDataCreator(PointRepository pointRepository, ProductRepository productRepository) {
        this.pointRepository = pointRepository;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void createTestData(){
        pointRepository.save(new Point(1L, 10000L));

        Product product1 = new Product(100L, 100L);
        Product product2 = new Product(100L, 200L);

        productRepository.save(product1);
        productRepository.save(product2);
    }
}
