package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

        @Test
        void availableGtZero_decrementsAvailableAndSaveProduct() {
            //Arrange
            product.setAvailable(10);
            product.setLeadTime(5);

            //Act
            productService.handleNormalProduct(product);

            //Assert
            assertThat(product.getAvailable()).isEqualTo(9);
            verify(productRepository).save(product);
        }

        @Test
        void availableEqZeroAndLeadTimeGtZero_sendDelayNotification(){
            //Arrange
            product.setAvailable(0);
            product.setLeadTime(5);

            //Act
            productService.handleNormalProduct(product);

            //Assert
            assertThat(product.getAvailable()).isEqualTo(0);
            verify(notificationService).sendDelayNotification(product.getLeadTime(), product.getName());
        }

        @Test
        void availableEqZeroAndLeadTimeElZero_doNothing(){
            //Arrange
            product.setAvailable(0);
            product.setLeadTime(0);

            //Act
            productService.handleNormalProduct(product);

            //Assert
            verify(productRepository, never()).save(product);
            verify(notificationService, never()).sendDelayNotification(product.getLeadTime(), product.getName());
        }

        @Test
        void availableLtZero_throwsIllegalArgumentException(){
            //Arrange
            product.setAvailable(-1);
            product.setLeadTime(0);

            //Act && Assert
            assertThatThrownBy(() -> productService.handleNormalProduct(product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Available quantity cannot be negative");
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

        @Test
        void inSeasonAndAvailableGtZero_decrementsAvailableAndSaveProduct(){
            //Arrange
            product.setSeasonStartDate(LocalDate.now().minusDays(1));
            product.setSeasonEndDate(LocalDate.now().plusDays(1));
            product.setAvailable(10);

            //Act
            productService.handleSeasonalProduct(product);

            //Assert
            assertThat(product.getAvailable()).isEqualTo(9);
            verify(productRepository).save(product);
        }

        @Test
        void seasonEndsBeforeLeadTime_setAvailableToZeroAndSendOutOfStockNotificationAndSaveProduct(){
            //Arrange
            product.setAvailable(10);
            product.setLeadTime(10);
            product.setSeasonStartDate(LocalDate.now().minusDays(2));
            product.setSeasonEndDate(LocalDate.now().minusDays(9));

            //Act
            productService.handleSeasonalProduct(product);

            //Assert
            assertThat(product.getAvailable()).isEqualTo(0);
            verify(notificationService).sendOutOfStockNotification(product.getName());
            verify(productRepository).save(product);
        }

        @Test
        void seasonInTheFuture_saveProductAndSendOutOfStockNotification(){
            //Arrange
            product.setAvailable(10);
            product.setLeadTime(10);
            product.setSeasonStartDate(LocalDate.now().plusDays(1));
            product.setSeasonEndDate(LocalDate.now().plusDays(10));

            //Act
            productService.handleSeasonalProduct(product);

            //Assert
            assertThat(product.getAvailable()).isEqualTo(10);
            verify(notificationService).sendOutOfStockNotification(product.getName());
            verify(productRepository).save(product);

        }

        @Test
        void inSeasonAndAvailableEqZeroAndLeadTimeBeforeSeasonEnds_sendDelayNotification(){
            //Arrange
            product.setAvailable(0);
            product.setLeadTime(10);
            product.setSeasonStartDate(LocalDate.now().minusDays(1));
            product.setSeasonEndDate(LocalDate.now().plusDays(20));

            //Act
            productService.handleSeasonalProduct(product);

            //Assert
            verify(notificationService).sendDelayNotification(product.getLeadTime(), product.getName());
            verify(productRepository, never()).save(product);
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