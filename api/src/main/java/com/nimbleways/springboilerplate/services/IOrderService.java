package com.nimbleways.springboilerplate.services;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;

public interface IOrderService {

    ProcessOrderResponse processOrder(Long orderId);
}
