package com.rapidtech.orderservice.repository;

import com.rapidtech.orderservice.model.OrderLineItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemsRepo extends JpaRepository<OrderLineItems, Long> {
}
