package com.nimbleways.springboilerplate.services.api;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;

public interface OrderService {
    Order findById(Long orderId);
    ProcessOrderResponse processOrder(Long orderId);
}
