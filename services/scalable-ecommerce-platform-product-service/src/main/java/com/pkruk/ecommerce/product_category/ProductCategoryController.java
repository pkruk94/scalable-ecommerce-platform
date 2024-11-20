package com.pkruk.ecommerce.product_category;

import com.pkruk.ecommerce.product_category.dto.NewProductCategoryRequest;
import com.pkruk.ecommerce.product_category.dto.ProductCategoryResponse;
import com.pkruk.ecommerce.product_category.dto.UpdateProductCategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product/category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping("/addNewCategory")
    public ResponseEntity<Long> addNewCategory(
            @RequestBody @Valid NewProductCategoryRequest newProductCategoryRequest) {
        return ResponseEntity.ok(
                productCategoryService.addNewProductCategory(newProductCategoryRequest));
    }

    @PutMapping("/updateProductCategory")
    public ResponseEntity<Long> updateProductCategory(
            @RequestBody @Valid UpdateProductCategoryRequest updateProductCategoryRequest) {
        return ResponseEntity.ok(
                productCategoryService.updateProductCategory(updateProductCategoryRequest)
        );
    }

    @GetMapping("/{product-category-id}")
    public ResponseEntity<ProductCategoryResponse> getProductCategory(@PathVariable(name = "product-category-id") Long productCategoryId) {
        return ResponseEntity.ok(
                productCategoryService.getProductCategoryById(productCategoryId)
        );
    }

    // TODO pagination
    @GetMapping
    public ResponseEntity<List<ProductCategoryResponse>> getAllProductCategory() {
        return ResponseEntity.ok(
                productCategoryService.getAllProductCategories()
        );
    }

    @DeleteMapping("/{product-category-id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable(name = "product-category-id") Long productCategoryId) {
        productCategoryService.deleteProductCategoryById(productCategoryId);
        return ResponseEntity.noContent().build();
    }
}
