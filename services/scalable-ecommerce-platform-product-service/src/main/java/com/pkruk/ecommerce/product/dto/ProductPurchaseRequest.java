package com.pkruk.ecommerce.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequest(
        @NotNull(message = "Product ID cannot be null")
        Long productId,

        @Positive(message = "Given quantity must be positive")
        Integer quantity
) {
}
