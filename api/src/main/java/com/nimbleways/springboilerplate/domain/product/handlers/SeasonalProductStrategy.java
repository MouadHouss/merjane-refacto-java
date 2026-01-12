package com.nimbleways.springboilerplate.domain.product.handlers;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.dto.product.ProductType;
import com.nimbleways.springboilerplate.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class SeasonalProductStrategy implements ProductStrategy {

    @Override
    public ProductType supportedProductType() {
        return ProductType.SEASONAL;
    }

    @Override
    public ProductOutcome handle(Product p) {
        if (p.isInSeason() && p.isAvailable()) {
            p.decreaseStock();
            return ProductOutcome.STOCK_DECREMENTED;
        }
        if (!p.canBeDeliveredBeforeSeasonEnd()) {
            p.removeStock();
            return ProductOutcome.STOCK_REMOVED;
        }
        if (p.isSeasonNotStarted()) {
            return ProductOutcome.OUT_OF_STOCK;
        }
        return ProductOutcome.DELAYED;
    }
}
