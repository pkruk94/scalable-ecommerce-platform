package com.pkruk.ecommerce.product_category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateProductCategoryRequest(
        @NotNull(message = "Product category ID is required")
        @Positive(message = "Product category ID must be a positive number")
        Long productCategoryID,

        @NotBlank(message = "Product category name is required")
        String name,

        @NotBlank(message = "Product category description is required")
        String description
) {
}
