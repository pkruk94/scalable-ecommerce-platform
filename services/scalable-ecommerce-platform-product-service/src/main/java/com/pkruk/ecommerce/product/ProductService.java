package com.pkruk.ecommerce.product;

import com.pkruk.ecommerce.exception.ProductPurchaseException;
import com.pkruk.ecommerce.product.dto.*;
import com.pkruk.ecommerce.product.mapper.ProductMapper;
import com.pkruk.ecommerce.product_category.ProductCategory;
import com.pkruk.ecommerce.product_category.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCategoryRepository productCategoryRepository;

    public Long createProduct(CreateProductRequest createProductRequest) {
        Product newProduct = productMapper.fromCreateProductRequestToProduct(createProductRequest);
        return productRepository.save(newProduct).getId();
    }

    public Long updateProduct(UpdateProductRequest updateProductRequest) {
        Product productToUpdate =
                productRepository.findById(updateProductRequest.productId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Product with id %s not found", updateProductRequest.productId())
                        ));

        if (!Objects.equals(productToUpdate.getProductCategory().getId(), updateProductRequest.categoryId())) {
            ProductCategory newProductCategory = productCategoryRepository
                    .findById(updateProductRequest.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("Product category with id %s not found", updateProductRequest.categoryId())
                    ));

            productToUpdate.setProductCategory(newProductCategory);
        }

        productToUpdate.setName(updateProductRequest.name());
        productToUpdate.setDescription(updateProductRequest.description());
        productToUpdate.setPrice(updateProductRequest.price());
        productToUpdate.setAvailableQuantity(updateProductRequest.availableQuantity());

        return productRepository.save(productToUpdate).getId();
    }

    public ProductResponse getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .map(productMapper::fromProductToProductResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Product with id %s not found", productId)
                ));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(productMapper::fromProductToProductResponse)
                .toList();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> productPurchaseRequestList) {
        var productsIds = productPurchaseRequestList.stream().map(ProductPurchaseRequest::productId).toList();
        var storedProducts = productRepository.findAllByIdInOrderById(productsIds);

        if (productsIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }

        var orderedProductsToPurchase = productPurchaseRequestList
                .stream()
                .sorted(Comparator
                        .comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < orderedProductsToPurchase.size(); i++) {
            var productToPurchase = orderedProductsToPurchase.get(i);
            var storedProduct = storedProducts.get(i);

            if (storedProduct.getAvailableQuantity() < productToPurchase.quantity()) {
                throw new ProductPurchaseException("One or more products do not have enough stock");
            }

            var newAvailableQuantity = storedProduct.getAvailableQuantity() - productToPurchase.quantity();
            storedProduct.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(storedProduct);
            purchasedProducts.add(productMapper.fromProductToProductPurchaseResponse(storedProduct, productToPurchase.quantity()));
        }
        return purchasedProducts;
    }

    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }
}
