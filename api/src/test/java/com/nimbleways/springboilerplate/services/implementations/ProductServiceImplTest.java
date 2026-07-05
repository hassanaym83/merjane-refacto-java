package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private Product product;

    @Mock
    ProductRepository productRepository;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Nested
    @DisplayName("Tests for handleNormalProduct method")
    class HandleNormalProduct {
        @BeforeEach
        void setUp() {
            product.setType(ProductType.NORMAL);
            product.setName("Normal Product");
        }
    }

    @Nested
    @DisplayName("Tests for handleSeasonalProduct method")
    class HandleSeasonalProduct {
        @BeforeEach
        void setUp() {
            product.setType(ProductType.SEASONAL);
            product.setName("Seasonal Product");
        }
    }

    @Nested
    @DisplayName("Tests for handleExpirableProduct method")
    class HandleExpirableProduct {
        @BeforeEach
        void setUp() {
            product.setType(ProductType.NORMAL);
            product.setName("Expirable Product");
        }
    }

}