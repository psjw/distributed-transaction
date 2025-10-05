package com.psjw.product.appliaction;

import com.psjw.product.appliaction.dto.ProductReserveCommand;
import com.psjw.product.appliaction.dto.ProductReserveResult;
import com.psjw.product.domain.Product;
import com.psjw.product.domain.ProductReservation;
import com.psjw.product.infrastucture.ProductRepository;
import com.psjw.product.infrastucture.ProductReservationRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductReservationRepository productReservationRepository;

    public ProductService(ProductRepository productRepository,
            ProductReservationRepository productReservationRepository) {
        this.productRepository = productRepository;
        this.productReservationRepository = productReservationRepository;
    }

    @Transactional
    public ProductReserveResult tryReserve(ProductReserveCommand command) {
        List<ProductReservation> exists = productReservationRepository.findAllByRequestId(command.requestId());

        if(!exists.isEmpty()) {
            long totalPrice = exists.stream().mapToLong(ProductReservation::getReservedPrice).sum();

            return new ProductReserveResult(totalPrice);
        }

        Long totalPrice = 0L;
        for(ProductReserveCommand.ReserveItem item : command.items()){
            Product product = productRepository.findById(item.productId()).orElseThrow();

            Long price = product.reserve(item.reserveQuantity());
            totalPrice += price;

            productRepository.save(product);
            productReservationRepository.save(new ProductReservation(
                    command.requestId(),
                    item.productId(),
                    item.reserveQuantity(),
                    price
            ));
        }

        return new ProductReserveResult(totalPrice);
    }
}
