package com.nimbleways.springboilerplate.domain.product;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductStrategy {
    void handle(Product product);
}
