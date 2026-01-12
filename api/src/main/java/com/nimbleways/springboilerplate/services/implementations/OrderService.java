package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.IOrderService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository or;
    private final ProductService productService;

    public OrderService(OrderRepository or, ProductService ps, ProductService productService) {
        this.or = or;
        this.productService = productService;
    }


    @Override
    public ProcessOrderResponse processOrder(Long orderId) {
        Order order = or.findById(orderId).orElseThrow();
        Set<Product> products = order.getItems();
        for (Product p : products) {
            productService.processProduct(p);
        }

        return new ProcessOrderResponse(order.getId());
    }
}
