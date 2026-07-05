package com.nimbleways.springboilerplate.services.implementations;

import java.time.LocalDate;

import com.nimbleways.springboilerplate.services.api.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void handleNormalProduct(Product product) {
        if (product.getAvailable() > 0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
            return;
        }

        if(product.getAvailable() == 0 && product.getLeadTime()>0) {
            notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
            return;
        }

        if(product.getAvailable() < 0) {
            throw new IllegalArgumentException("Available quantity cannot be negative");
        }
    }

    @Override
    public void handleSeasonalProduct(Product product) {
        if(product.getSeasonStartDate().isBefore(LocalDate.now()) && product.getSeasonEndDate().isAfter(LocalDate.now()) && product.getAvailable()>0) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
            return;
        }

        if (LocalDate.now().plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate())) {
            notificationService.sendOutOfStockNotification(product.getName());
            product.setAvailable(0);
            productRepository.save(product);
            return;
        }

        if (product.getSeasonStartDate().isAfter(LocalDate.now())) {
            notificationService.sendOutOfStockNotification(product.getName());
            productRepository.save(product);
            return;
        }

        notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
    }

    @Override
    public void handleExpirableProduct(Product product) {
        if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
            product.setAvailable(product.getAvailable() - 1);
            productRepository.save(product);
            return;
        }

        product.setAvailable(0);
        notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
        productRepository.save(product);
    }

}