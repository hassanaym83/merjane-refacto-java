package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.api.OrderService;
import com.nimbleways.springboilerplate.services.api.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Order not found")
        );
    }

    @Override
    @Transactional
    public ProcessOrderResponse processOrder(Long orderId) {
        Order order = findById(orderId);

        order.getItems().forEach(product -> {
            switch (product.getType()) {
                case NORMAL:
                    productService.handleNormalProduct(product);
                    break;
                case SEASONAL:
                    productService.handleSeasonalProduct(product);
                    break;
                case EXPIRABLE:
                    productService.handleExpirableProduct(product);
                    break;
                default:
                    throw new RuntimeException("Unknown product type");
            }
        });
        return new ProcessOrderResponse(orderId);
    }
}
