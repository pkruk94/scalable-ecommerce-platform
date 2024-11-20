package com.pkruk.ecommerce.product.mapper;

import com.pkruk.ecommerce.product.Product;
import com.pkruk.ecommerce.product.dto.CreateProductRequest;
import com.pkruk.ecommerce.product.dto.ProductPurchaseResponse;
import com.pkruk.ecommerce.product.dto.ProductResponse;
import com.pkruk.ecommerce.product_category.ProductCategory;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product fromCreateProductRequestToProduct(CreateProductRequest createProductRequest) {
        return Product
                .builder()
                .name(createProductRequest.name())
                .description(createProductRequest.description())
                .availableQuantity(createProductRequest.availableQuantity())
                .price(createProductRequest.price())
                .productCategory(
                        ProductCategory
                                .builder()
                                .id(createProductRequest.categoryId())
                                .build()
                )
                .build();
    }

    public ProductResponse fromProductToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getProductCategory().getId(),
                product.getProductCategory().getName(),
                product.getProductCategory().getDescription()
        );
    }

    public ProductPurchaseResponse fromProductToProductPurchaseResponse(Product storedProduct, Integer quantity) {
        return new ProductPurchaseResponse(
                storedProduct.getId(),
                storedProduct.getName(),
                storedProduct.getDescription(),
                storedProduct.getPrice(),
                quantity
        );
    }
}
