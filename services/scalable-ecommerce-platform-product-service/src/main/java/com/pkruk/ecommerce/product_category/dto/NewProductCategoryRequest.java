package com.pkruk.ecommerce.product_category.dto;

import jakarta.validation.constraints.NotBlank;

public record NewProductCategoryRequest(
        @NotBlank(message = "Product category name is required")
        String name,

        @NotBlank(message = "Product category description is required")
        String description
) {
}
