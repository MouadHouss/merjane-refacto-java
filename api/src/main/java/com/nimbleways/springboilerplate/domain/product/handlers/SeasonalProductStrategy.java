package com.nimbleways.springboilerplate.domain.product.handlers;

import com.nimbleways.springboilerplate.domain.product.ProductStrategy;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeasonalProductStrategy implements ProductStrategy {

    private final ProductRepository pr;
    private final NotificationService ns;

    @Override
    public void handle(Product p) {

        if (p.isInSeason() && p.isAvailable()) {
            p.decreaseStock();
            pr.save(p);
            return;
        }
        if (!p.canBeDeliveredBeforeSeasonEnd()) {
            p.removeStock();
            pr.save(p);
            return;
        }
        if (p.isSeasonNotStarted()) {
            ns.sendOutOfStockNotification(p.getName());
            pr.save(p);
            return;
        }
        ns.sendDelayNotification(p.getLeadTime(), p.getName());
        pr.save(p);
    }
}
