package com.nimbleways.springboilerplate.domain.product;

import com.nimbleways.springboilerplate.domain.product.handlers.ExpirableProductStrategy;
import com.nimbleways.springboilerplate.domain.product.handlers.NormalProductStrategy;
import com.nimbleways.springboilerplate.domain.product.handlers.SeasonalProductStrategy;
import org.springframework.stereotype.Component;

@Component
public class ProductStrategyFactory {

    private final NormalProductStrategy normalStrategy;
    private final SeasonalProductStrategy seasonalStrategy;
    private final ExpirableProductStrategy expirableStrategy;

    public ProductStrategyFactory(NormalProductStrategy normalStrategy, SeasonalProductStrategy seasonalStrategy, ExpirableProductStrategy expirableStrategy) {
        this.normalStrategy = normalStrategy;
        this.seasonalStrategy = seasonalStrategy;
        this.expirableStrategy = expirableStrategy;
    }


    public ProductStrategy getProcessingStrategy(String productType) {
        return switch (productType.toUpperCase()) {
            case "NORMAL" -> normalStrategy;
            case "SEASONAL" -> seasonalStrategy;
            case "EXPIRABLE" -> expirableStrategy;
            default -> throw new IllegalArgumentException(
                    "Type de produit non supporté: " + productType
            );
        };
    }
}