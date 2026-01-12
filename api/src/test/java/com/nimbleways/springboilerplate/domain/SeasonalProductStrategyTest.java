package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.product.handlers.SeasonalProductStrategy;
import com.nimbleways.springboilerplate.dto.product.ProductOutcome;
import com.nimbleways.springboilerplate.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class SeasonalProductStrategyTest {

    @InjectMocks
    private SeasonalProductStrategy strategy;

    @Test
    void handle_whenInSeasonAndAvailable_shouldDecreaseStockAndSave() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(10);
        product.setLeadTime(5);
        product.setSeasonStartDate(LocalDate.now().minusDays(5));
        product.setSeasonEndDate(LocalDate.now().plusDays(10));
        product.setName("Watermelon");

        // WHEN
        ProductOutcome outcome = strategy.handle(product);

        // THEN
        assertEquals(9, product.getAvailable());
        assertEquals(ProductOutcome.STOCK_DECREMENTED, outcome);
    }


    @Test
    void handle_whenOutOfStockAndLeadTimeExceedsSeasonEnd_shouldNotifyOutOfStockAndRemoveStock() {
        Product product = new Product();
        product.setAvailable(0);
        product.setLeadTime(30);
        product.setSeasonStartDate(LocalDate.now().minusDays(10));
        product.setSeasonEndDate(LocalDate.now().plusDays(5));
        product.setName("Grapes");

        ProductOutcome outcome = strategy.handle(product);

        assertEquals(0, product.getAvailable());
        assertEquals(ProductOutcome.STOCK_REMOVED, outcome);
    }

    @Test
    void handle_whenOutOfStockButDelayWithinSeason_shouldSendDelayNotificationAndSave() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(0);
        product.setLeadTime(5);
        product.setSeasonStartDate(LocalDate.now().minusDays(5));
        product.setSeasonEndDate(LocalDate.now().plusDays(20));
        product.setName("Strawberry");

        // WHEN
        ProductOutcome outcome = strategy.handle(product);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(ProductOutcome.DELAYED, outcome);
    }

}