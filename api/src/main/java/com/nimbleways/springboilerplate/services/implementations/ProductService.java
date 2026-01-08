package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.domain.product.ProductStrategyFactory;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.IProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

    private final ProductStrategyFactory productStrategyFactory;

    public ProductService(ProductStrategyFactory productStrategyFactory) {

        this.productStrategyFactory = productStrategyFactory;
    }

    @Override
    public void processProduct(Product product) {
        ProductStrategy strategy = productStrategyFactory.getProcessingStrategy(product.getType());
        strategy.handle(product);
    }

}