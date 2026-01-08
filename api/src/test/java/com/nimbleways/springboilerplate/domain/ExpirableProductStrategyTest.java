package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.product.handlers.ExpirableProductStrategy;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ExpirableProductStrategyTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

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
        strategy.handle(product);

        // THEN
        assertEquals(4, product.getAvailable());
        verify(productRepository).save(product);
        verify(notificationService, never())
                .sendExpirationNotification(anyString(), any());
    }

    @Test
    void handle_whenExpired_shouldNotifyAndRemoveStock() {
        // GIVEN
        Product product = new Product();
        product.setAvailable(10);
        product.setExpiryDate(LocalDate.now().minusDays(1));
        product.setName("Milk");

        // WHEN
        strategy.handle(product);

        // THEN
        assertEquals(0, product.getAvailable());
        verify(notificationService)
                .sendExpirationNotification("Milk", product.getExpiryDate());
        verify(productRepository).save(product);
    }


}