package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.api.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private Order order;
    private Product product;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductService productService;

    @InjectMocks
    OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);

        product = new Product();
    }

    @Nested
    @DisplayName("processOrder method tests")
    class ProcessOrder {
        @Test
        void normalProduct_delegateHandleNormalProduct(){
            //Arrange
            product.setType(ProductType.NORMAL);
            order.setItems(Set.of(product));
            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

            //Act
            orderService.processOrder(order.getId());

            //Assert
            verify(productService).handleNormalProduct(product);
            verifyNoMoreInteractions(productService);
        }

        @Test
        void seasonalProduct_delegateHandleSeasonalProduct(){
            //Arrange
            product.setType(ProductType.SEASONAL);
            order.setItems(Set.of(product));
            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

            //Act
            orderService.processOrder(order.getId());

            //Assert
            verify(productService).handleSeasonalProduct(product);
            verifyNoMoreInteractions(productService);
        }

        @Test
        void expirableProduct_delegateHandleExpirableProduct(){
            //Arrange
            product.setType(ProductType.EXPIRABLE);
            order.setItems(Set.of(product));
            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

            //Act
            orderService.processOrder(order.getId());

            //Assert
            verify(productService).handleExpirableProduct(product);
            verifyNoMoreInteractions(productService);
        }


    }

}