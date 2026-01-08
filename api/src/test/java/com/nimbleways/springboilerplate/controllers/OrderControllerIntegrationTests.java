package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Specify the controller class you want to test
// This indicates to spring boot to only load UsersController into the context
// Which allows a better performance and needs to do less mocks
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void processOrderShouldReturn() throws Exception {
        // GIVEN
        List<Product> products = createProducts();
        productRepository.saveAll(products);

        Order order = createOrder(new HashSet<>(products));
        order = orderRepository.save(order);

        // WHEN
        mockMvc.perform(post("/orders/{orderId}/processOrder", order.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());

        // THEN — DB assertions
        Product usbCable = findByName("USB Cable");
        Assertions.assertEquals(29, usbCable.getAvailable(),
                "NORMAL product with stock should be decremented");

        Product butter = findByName("Butter");
        Assertions.assertEquals(29, butter.getAvailable(),
                "EXPIRABLE product not expired should be decremented");

        Product milk = findByName("Milk");
        Assertions.assertEquals(0, milk.getAvailable(),
                "Expired product should be set to unavailable");

        Product watermelon = findByName("Watermelon");
        Assertions.assertEquals(29, watermelon.getAvailable(),
                "Seasonal product in season should be decremented");

        Product grapes = findByName("Grapes");
        Assertions.assertEquals(30, grapes.getAvailable(),
                "Seasonal product outside season should not be decremented");

        // THEN — notification assertions
        Mockito.verify(notificationService)
                .sendExpirationNotification(eq("Milk"), eq(milk.getExpiryDate()));

        Mockito.verify(notificationService)
                .sendOutOfStockNotification(eq("Grapes"));

        Mockito.verify(notificationService, never())
                .sendDelayNotification(anyInt(), eq("Grapes"));
    }

    private static Order createOrder(Set<Product> products) {
        Order order = new Order();
        order.setItems(products);
        return order;
    }

    private static List<Product> createProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(null, 15, 30, "NORMAL", "USB Cable", null, null, null));
        products.add(new Product(null, 10, 0, "NORMAL", "USB Dongle", null, null, null));
        products.add(new Product(null, 15, 30, "EXPIRABLE", "Butter", LocalDate.now().plusDays(26), null,
                null));
        products.add(new Product(null, 90, 6, "EXPIRABLE", "Milk", LocalDate.now().minusDays(2), null, null));
        products.add(new Product(null, 15, 30, "SEASONAL", "Watermelon", null, LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(58)));
        products.add(new Product(null, 15, 30, "SEASONAL", "Grapes", null, LocalDate.now().plusDays(180),
                LocalDate.now().plusDays(240)));
        return products;
    }

    private Product findByName(String name) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
