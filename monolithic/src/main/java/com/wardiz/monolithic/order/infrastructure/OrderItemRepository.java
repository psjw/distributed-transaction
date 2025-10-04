package com.wardiz.monolithic.order.infrastructure;

import com.wardiz.monolithic.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
