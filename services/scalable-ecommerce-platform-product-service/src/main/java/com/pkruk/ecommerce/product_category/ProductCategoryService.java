package com.pkruk.ecommerce.product_category;

import com.pkruk.ecommerce.product_category.dto.NewProductCategoryRequest;
import com.pkruk.ecommerce.product_category.dto.ProductCategoryResponse;
import com.pkruk.ecommerce.product_category.dto.UpdateProductCategoryRequest;
import com.pkruk.ecommerce.product_category.mapper.ProductCategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    public Long addNewProductCategory(NewProductCategoryRequest newProductCategoryRequest) {
        ProductCategory productCategory =
                productCategoryMapper.fromNewProductCategoryToProductCategory(newProductCategoryRequest);
        return productCategoryRepository.save(productCategory).getId();
    }

    public Long updateProductCategory(UpdateProductCategoryRequest updateProductCategoryRequest) {
        ProductCategory productCategoryToUpdate =
                productCategoryRepository.findById(updateProductCategoryRequest.productCategoryID())
                        .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Product category with id %s not found", updateProductCategoryRequest.productCategoryID())
                        ));

        productCategoryToUpdate.setName(updateProductCategoryRequest.name());
        productCategoryToUpdate.setDescription(updateProductCategoryRequest.description());

        return productCategoryRepository.save(productCategoryToUpdate).getId();
    }

    public ProductCategoryResponse getProductCategoryById(Long productCategoryId) {
        return productCategoryRepository.findById(productCategoryId)
                .map(productCategoryMapper::fromProductCategoryToProductCategoryResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Product category with id %s not found", productCategoryId)
                ));
    }

    public List<ProductCategoryResponse> getAllProductCategories() {
        return productCategoryRepository
                .findAll()
                .stream()
                .map(productCategoryMapper::fromProductCategoryToProductCategoryResponse)
                .toList();
    }

    public void deleteProductCategoryById(Long productCategoryId) {
        productCategoryRepository.deleteById(productCategoryId);
    }
}
