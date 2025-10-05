package com.psjw.product.infrastucture;

import com.psjw.product.domain.ProductReservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReservationRepository extends JpaRepository<ProductReservation, Long> {

    List<ProductReservation> findAllByRequestId(Long requestId);
}
