package com.pkruk.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotBlank(message = "Product description cannot be blank")
        String description,

        @Positive(message = "Product available quantity must be a positive value")
        Integer availableQuantity,

        @Positive(message = "Product price must be a positive value")
        BigDecimal price,

        @NotNull(message = "Product category is required")
        Long categoryId

) {
}
