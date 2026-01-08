package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.product.handlers.NormalProductStrategy;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class NormalProductStrategyTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

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
        strategy.handle(product);

        // THEN
        assertEquals(4, product.getAvailable());
        verify(productRepository).save(product);
        verify(notificationService, never())
                .sendDelayNotification(anyInt(), anyString());
    }


    @Test
    void handle_whenOutOfStockAndLeadTimePositive_shouldSendDelayNotification() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(0);
        product.setLeadTime(7);
        product.setName("USB Hub");

        // WHEN
        strategy.handle(product);

        // THEN
        verify(notificationService)
                .sendDelayNotification(7, "USB Hub");

        verify(productRepository).save(product);
        assertEquals(0, product.getAvailable());
    }

    @Test
    void handle_whenOutOfStockAndNoLeadTime_shouldOnlySaveProduct() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(0);
        product.setLeadTime(0);
        product.setName("USB Adapter");

        // WHEN
        strategy.handle(product);

        // THEN
        verify(notificationService, never())
                .sendDelayNotification(anyInt(), anyString());

        verify(productRepository).save(product);
        assertEquals(0, product.getAvailable());
    }

}
