package com.nimbleways.springboilerplate.domain.product.handlers;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.dto.product.ProductType;
import com.nimbleways.springboilerplate.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ExpirableProductStrategy implements ProductStrategy {

    @Override
    public ProductType supportedProductType() {
        return ProductType.EXPIRABLE;
    }

    @Override
    public ProductOutcome handle(Product p) {
        if (p.isAvailable() && p.isNotExpired()) {
            p.decreaseStock();
            return ProductOutcome.STOCK_DECREMENTED;
        } else {
            p.removeStock();
            return ProductOutcome.EXPIRED;
        }
    }
}
