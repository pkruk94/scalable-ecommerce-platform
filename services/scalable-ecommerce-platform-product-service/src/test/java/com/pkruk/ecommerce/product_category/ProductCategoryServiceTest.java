package com.pkruk.ecommerce.product_category;

import com.pkruk.ecommerce.product_category.dto.NewProductCategoryRequest;
import com.pkruk.ecommerce.product_category.dto.ProductCategoryResponse;
import com.pkruk.ecommerce.product_category.dto.UpdateProductCategoryRequest;
import com.pkruk.ecommerce.product_category.mapper.ProductCategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCategoryMapper productCategoryMapper;

    @InjectMocks
    private ProductCategoryService productCategoryService;

    private ProductCategory productCategory;
    private ProductCategoryResponse productCategoryResponse;
    private NewProductCategoryRequest newProductCategoryRequest;
    private UpdateProductCategoryRequest updateProductCategoryRequest;

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Test Category";
    private static final String CATEGORY_DESCRIPTION = "Test Description";
    private static final String UPDATED_NAME = "Updated Category";
    private static final String UPDATED_DESCRIPTION = "Updated Description";

    @BeforeEach
    void setUp() {
        productCategory = ProductCategory.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .products(new ArrayList<>())
                .build();

        productCategoryResponse = new ProductCategoryResponse(
                CATEGORY_ID,
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION
        );

        newProductCategoryRequest = new NewProductCategoryRequest(
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION
        );

        updateProductCategoryRequest = new UpdateProductCategoryRequest(
                CATEGORY_ID,
                UPDATED_NAME,
                UPDATED_DESCRIPTION
        );
    }

    @Test
    void addNewProductCategory_ShouldReturnId() {
        // given
        ProductCategory mappedCategory = ProductCategory.builder()
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .products(new ArrayList<>())
                .build();

        ProductCategory savedCategory = ProductCategory.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .products(new ArrayList<>())
                .build();

        when(productCategoryMapper.fromNewProductCategoryToProductCategory(newProductCategoryRequest))
                .thenReturn(mappedCategory);
        when(productCategoryRepository.save(mappedCategory)).thenReturn(savedCategory);

        // when
        Long result = productCategoryService.addNewProductCategory(newProductCategoryRequest);

        // then
        assertThat(result).isEqualTo(CATEGORY_ID);
        verify(productCategoryRepository).save(mappedCategory);
        verify(productCategoryMapper).fromNewProductCategoryToProductCategory(newProductCategoryRequest);
    }

    @Test
    void updateProductCategory_WhenExists_ShouldUpdateAndReturnId() {
        // given
        when(productCategoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(productCategory));
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(productCategory);

        // when
        Long result = productCategoryService.updateProductCategory(updateProductCategoryRequest);

        // then
        assertThat(result).isEqualTo(CATEGORY_ID);
        assertThat(productCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(productCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        verify(productCategoryRepository).findById(CATEGORY_ID);
        verify(productCategoryRepository).save(productCategory);
    }

    @Test
    void updateProductCategory_WhenNotExists_ShouldThrowException() {
        // given
        when(productCategoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productCategoryService.updateProductCategory(updateProductCategoryRequest));

        assertThat(exception.getMessage())
                .isEqualTo(String.format("Product category with id %s not found", CATEGORY_ID));
        verify(productCategoryRepository).findById(CATEGORY_ID);
        verify(productCategoryRepository, never()).save(any());
    }

    @Test
    void getProductCategoryById_WhenExists_ShouldReturnResponse() {
        // given
        when(productCategoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(productCategory));
        when(productCategoryMapper.fromProductCategoryToProductCategoryResponse(productCategory))
                .thenReturn(productCategoryResponse);

        // when
        ProductCategoryResponse result = productCategoryService.getProductCategoryById(CATEGORY_ID);

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(productCategoryResponse);

        verify(productCategoryRepository).findById(CATEGORY_ID);
        verify(productCategoryMapper).fromProductCategoryToProductCategoryResponse(productCategory);
    }

    @Test
    void getProductCategoryById_WhenNotExists_ShouldThrowException() {
        // given
        when(productCategoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productCategoryService.getProductCategoryById(CATEGORY_ID));

        assertThat(exception.getMessage())
                .isEqualTo(String.format("Product category with id %s not found", CATEGORY_ID));
        verify(productCategoryRepository).findById(CATEGORY_ID);
        verify(productCategoryMapper, never()).fromProductCategoryToProductCategoryResponse(any());
    }

    @Test
    void getAllProductCategories_ShouldReturnList() {
        // given
        List<ProductCategory> categories = List.of(productCategory);
        when(productCategoryRepository.findAll()).thenReturn(categories);
        when(productCategoryMapper.fromProductCategoryToProductCategoryResponse(productCategory))
                .thenReturn(productCategoryResponse);

        // when
        List<ProductCategoryResponse> result = productCategoryService.getAllProductCategories();

        // then
        assertThat(result)
                .hasSize(1)
                .containsExactly(productCategoryResponse);

        verify(productCategoryRepository).findAll();
        verify(productCategoryMapper).fromProductCategoryToProductCategoryResponse(productCategory);
    }

    @Test
    void getAllProductCategories_WhenNoCategories_ShouldReturnEmptyList() {
        // given
        when(productCategoryRepository.findAll()).thenReturn(List.of());

        // when
        List<ProductCategoryResponse> result = productCategoryService.getAllProductCategories();

        // then
        assertThat(result).isEmpty();
        verify(productCategoryRepository).findAll();
        verify(productCategoryMapper, never()).fromProductCategoryToProductCategoryResponse(any());
    }

    @Test
    void deleteProductCategoryById_ShouldDeleteCategory() {
        // when
        productCategoryService.deleteProductCategoryById(CATEGORY_ID);

        // then
        verify(productCategoryRepository).deleteById(CATEGORY_ID);
    }
}
