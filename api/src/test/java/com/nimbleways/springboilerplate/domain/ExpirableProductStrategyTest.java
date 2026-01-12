package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.product.handlers.ExpirableProductStrategy;
import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class ExpirableProductStrategyTest {

    @InjectMocks
    private ExpirableProductStrategy strategy;

    @Test
    void handle_whenAvailableAndNotExpired_shouldDecreaseStockAndSave() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(5);
        product.setExpiryDate(LocalDate.now().plusDays(3));
        product.setName("Butter");

        // WHEN
        ProductOutcome outcome = strategy.handle(product);

        // THEN
        assertEquals(4, product.getAvailable());
        assertEquals(ProductOutcome.STOCK_DECREMENTED, outcome);
    }

    @Test
    void handle_whenExpired_shouldNotifyAndRemoveStock() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(10);
        product.setExpiryDate(LocalDate.now().minusDays(1));
        product.setName("Milk");

        // WHEN
        ProductOutcome outcome = strategy.handle(product);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(ProductOutcome.EXPIRED, outcome);
    }


}