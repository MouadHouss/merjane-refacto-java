package com.nimbleways.springboilerplate.domain.product.handlers;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class ExpirableProductStrategy implements ProductStrategy {

    private final ProductRepository pr;
    private final NotificationService ns;

    public ExpirableProductStrategy(ProductRepository pr, NotificationService ns) {
        this.pr = pr;
        this.ns = ns;
    }

    @Override
    public void handle(Product p) {
        if (p.isAvailable() && p.isNotExpired()) {
            p.decreaseStock();
            pr.save(p);
        } else {
            ns.sendExpirationNotification(p.getName(), p.getExpiryDate());
            p.removeStock();
            pr.save(p);
        }
    }
}
