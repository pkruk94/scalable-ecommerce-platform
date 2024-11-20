package com.pkruk.ecommerce.product_category.mapper;

import com.pkruk.ecommerce.product_category.ProductCategory;
import com.pkruk.ecommerce.product_category.dto.NewProductCategoryRequest;
import com.pkruk.ecommerce.product_category.dto.ProductCategoryResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryMapper {
    public ProductCategory fromNewProductCategoryToProductCategory(NewProductCategoryRequest newProductCategoryRequest) {
        return ProductCategory.builder()
                .name(newProductCategoryRequest.name())
                .description(newProductCategoryRequest.description())
                .build();
    }

    public ProductCategoryResponse fromProductCategoryToProductCategoryResponse(ProductCategory productCategory) {
        return new ProductCategoryResponse(
                productCategory.getId(),
                productCategory.getName(),
                productCategory.getDescription()
        );
    }
}
