package com.pkruk.ecommerce.product;

import com.pkruk.ecommerce.product.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<Long> createProduct(
            @RequestBody @Valid CreateProductRequest createProductRequest) {
        return ResponseEntity.ok(productService.createProduct(createProductRequest));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<Long> updateProduct(
            @RequestBody @Valid UpdateProductRequest updateProductRequest
    ) {
        return ResponseEntity.ok(
                productService.updateProduct(updateProductRequest)
        );
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable(name = "product-id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    // TODO pagination
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> createPurchase(List<ProductPurchaseRequest> productPurchaseRequestList) {
        return ResponseEntity.ok(productService.purchaseProducts(productPurchaseRequestList));
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "product-id") Long productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }
}
