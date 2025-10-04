package com.psjw.monolithic.order.infrastructure;

import com.psjw.monolithic.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
