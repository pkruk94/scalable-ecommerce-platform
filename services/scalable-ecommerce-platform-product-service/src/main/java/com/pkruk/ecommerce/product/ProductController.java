package com.pkruk.ecommerce.product;

import com.pkruk.ecommerce.product.dto.CreateProductRequest;
import com.pkruk.ecommerce.product.dto.ProductPurchaseRequest;
import com.pkruk.ecommerce.product.dto.ProductPurchaseResponse;
import com.pkruk.ecommerce.product.dto.ProductResponse;
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

    @PostMapping
    public ResponseEntity<Long> createProduct(
            @RequestBody @Valid CreateProductRequest createProductRequest) {
        return ResponseEntity.ok(productService.createProduct(createProductRequest));
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

}
