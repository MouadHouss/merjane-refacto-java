package com.nimbleways.springboilerplate.domain.product.handlers;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.dto.product.ProductType;
import com.nimbleways.springboilerplate.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class NormalProductStrategy implements ProductStrategy {

    @Override
    public ProductType supportedProductType() {
        return ProductType.NORMAL;
    }

    @Override
    public ProductOutcome handle(Product p) {
        if (p.isAvailable()) {
            p.decreaseStock();
            return ProductOutcome.STOCK_DECREMENTED;
        } else if (p.getLeadTime() > 0) {
            return ProductOutcome.DELAYED;
        }
        return ProductOutcome.OUT_OF_STOCK;
    }
}
