package com.csis;

public record Order(
    int orderId, 
    String orderNo, 
    double totalAmount
) {}