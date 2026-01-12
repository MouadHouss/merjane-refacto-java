package com.nimbleways.springboilerplate.domain.product;

import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.dto.product.ProductType;
import com.nimbleways.springboilerplate.entities.Product;

public interface ProductStrategy {

    ProductType supportedProductType();

    ProductOutcome handle(Product product);
}
