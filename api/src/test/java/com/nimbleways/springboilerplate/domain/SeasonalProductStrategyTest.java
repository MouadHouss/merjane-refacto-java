package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.product.handlers.SeasonalProductStrategy;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class SeasonalProductStrategyTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

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
        strategy.handle(product);

        // THEN
        assertEquals(9, product.getAvailable());
        verify(productRepository).save(product);
        verify(notificationService, never())
                .sendOutOfStockNotification(anyString());
        verify(notificationService, never())
                .sendDelayNotification(anyInt(), anyString());
    }


    @Test
    void handle_whenOutOfStockAndLeadTimeExceedsSeasonEnd_shouldNotifyOutOfStockAndRemoveStock() {
        Product product = new Product();
        product.setAvailable(0); // ✅ rupture
        product.setLeadTime(30);
        product.setSeasonStartDate(LocalDate.now().minusDays(10));
        product.setSeasonEndDate(LocalDate.now().plusDays(5));
        product.setName("Grapes");

        strategy.handle(product);

        assertEquals(0, product.getAvailable());
        verify(productRepository).save(product);
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
        strategy.handle(product);

        // THEN
        verify(notificationService)
                .sendDelayNotification(5, "Strawberry");
        verify(productRepository).save(product);
        assertEquals(0, product.getAvailable());
    }

}