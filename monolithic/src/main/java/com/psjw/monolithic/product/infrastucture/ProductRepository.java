package com.psjw.monolithic.product.infrastucture;

import com.psjw.monolithic.product.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductById(Long id);
}
