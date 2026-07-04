package com.csis;

public record Product(
    int productId, 
    String productName, 
    double unitPrice
) {}