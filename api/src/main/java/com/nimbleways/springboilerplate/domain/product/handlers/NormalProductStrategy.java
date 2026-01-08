package com.nimbleways.springboilerplate.domain.product.handlers;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NormalProductStrategy implements ProductStrategy {

    private final ProductRepository pr;
    private final NotificationService ns;

    @Override
    public void handle(Product p) {

        if (p.isAvailable()) {
            p.decreaseStock();
            pr.save(p);
        } else {
            if (p.getLeadTime() > 0) {
                ns.sendDelayNotification(p.getLeadTime(), p.getName());
            }
        }
    }
}
