package com.wardiz.monolithic.order.infrastructure;

import com.wardiz.monolithic.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
