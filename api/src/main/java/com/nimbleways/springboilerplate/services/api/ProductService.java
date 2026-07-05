package com.nimbleways.springboilerplate.services.api;

import com.nimbleways.springboilerplate.entities.Product;

import java.time.LocalDate;

public interface ProductService {
    void handleNormalProduct(Product product);

    void handleSeasonalProduct(Product product);

    void handleExpirableProduct(Product product);
}
