package com.nimbleways.springboilerplate.domain.product;

import com.nimbleways.springboilerplate.dto.product.ProductType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductStrategyFactory {

    private final List<ProductStrategy> productStrategies;

    public ProductStrategyFactory(List<ProductStrategy> productStrategies) {
        this.productStrategies = productStrategies;
    }

    public ProductStrategy getProcessingStrategy(ProductType productType) {
        return productStrategies.stream()
                .filter(p -> p.supportedProductType().equals(productType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for product type: " + productType));
    }
}