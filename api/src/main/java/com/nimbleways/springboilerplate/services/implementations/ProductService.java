package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.domain.product.ProductStrategyFactory;
import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

    private final ProductStrategyFactory productStrategyFactory;
    private final NotificationService ns;
    private final ProductRepository pr;

    public ProductService(ProductStrategyFactory productStrategyFactory, NotificationService ns, ProductRepository pr) {

        this.productStrategyFactory = productStrategyFactory;
        this.ns = ns;
        this.pr = pr;
    }

    @Override
    public void processProduct(Product product) {

        ProductStrategy strategy = productStrategyFactory.getProcessingStrategy(product.getType());
        ProductOutcome outcome = strategy.handle(product);
        pr.save(product);
        switch (outcome) {
            case DELAYED -> ns.sendDelayNotification(
                    product.getLeadTime(),
                    product.getName()
            );
            case OUT_OF_STOCK -> ns.sendOutOfStockNotification(
                    product.getName()
            );
            case EXPIRED -> ns.sendExpirationNotification(
                    product.getName(),
                    product.getExpiryDate()
            );


            default -> {
                //DO NOTHING
            }
        }
    }
}