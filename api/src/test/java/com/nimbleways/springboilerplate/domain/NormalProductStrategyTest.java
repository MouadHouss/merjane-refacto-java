package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.product.handlers.NormalProductStrategy;
import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class NormalProductStrategyTest {


    @InjectMocks
    private NormalProductStrategy strategy;

    @Test
    void handle_whenProductAvailable_shouldDecreaseStockAndSave() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(5);
        product.setLeadTime(10);
        product.setName("USB Cable");

        // WHEN
        ProductOutcome outcome = strategy.handle(product);

        // THEN
        assertEquals(4, product.getAvailable());
        assertEquals(ProductOutcome.STOCK_DECREMENTED, outcome);
    }


    @Test
    void handle_whenOutOfStockAndLeadTimePositive_shouldSendDelayNotification() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(0);
        product.setLeadTime(7);
        product.setName("USB Hub");

        // WHEN
        ProductOutcome outcome = strategy.handle(product);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(ProductOutcome.DELAYED, outcome);
    }

    @Test
    void handle_whenOutOfStockAndNoLeadTime_shouldDoNothing() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(0);
        product.setLeadTime(0);
        product.setName("USB Adapter");

        // WHEN
        ProductOutcome outcome = strategy.handle(product);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(ProductOutcome.OUT_OF_STOCK, outcome);
    }

}
