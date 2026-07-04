package com.csis;

public record Ingredient(
    int ingredientId, 
    String ingredientName, 
    double quantityOnHand
) {}