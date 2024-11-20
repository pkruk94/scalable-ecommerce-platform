package com.pkruk.ecommerce.product;

import com.pkruk.ecommerce.exception.ProductPurchaseException;
import com.pkruk.ecommerce.product.dto.*;
import com.pkruk.ecommerce.product.mapper.ProductMapper;
import com.pkruk.ecommerce.product_category.ProductCategory;
import com.pkruk.ecommerce.product_category.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product savedProduct;
    private Product newProduct;
    private CreateProductRequest createProductRequest;
    private UpdateProductRequest updateProductRequest;
    private ProductResponse productResponse;
    private ProductPurchaseRequest productPurchaseRequest;
    private ProductPurchaseResponse productPurchaseResponse;
    private ProductCategory newProductCategory;

    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "Product Name";
    private static final String PRODUCT_DESCRIPTION = "Product Description";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(10.0);
    private static final int PRODUCT_QUANTITY = 1;
    private static final String PRODUCT_CATEGORY_NAME = "Product Category Name";
    private static final String PRODUCT_CATEGORY_DESCRIPTION = "Product Category Description";
    private static final Long NEW_PRODUCT_CATEGORY_ID = 2L;

    @BeforeEach
    void setUp() {
        ProductCategory productCategory = ProductCategory.builder()
                .description(PRODUCT_CATEGORY_DESCRIPTION)
                .name(PRODUCT_CATEGORY_NAME)
                .id(PRODUCT_ID)
                .build();

        savedProduct = Product
                .builder()
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .availableQuantity(PRODUCT_QUANTITY)
                .price(PRODUCT_PRICE)
                .productCategory(productCategory)
                .build();

        newProduct = Product
                .builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .availableQuantity(PRODUCT_QUANTITY)
                .price(PRODUCT_PRICE)
                .productCategory(productCategory)
                .build();

        productResponse = new ProductResponse(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION,
                PRODUCT_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_ID,
                PRODUCT_CATEGORY_NAME,
                PRODUCT_DESCRIPTION
        );

        updateProductRequest = new UpdateProductRequest(
                PRODUCT_ID,
                "Updated Name",
                "Updated Description",
                5,
                BigDecimal.valueOf(20.0),
                NEW_PRODUCT_CATEGORY_ID
        );

        newProductCategory = ProductCategory.builder()
                .id(NEW_PRODUCT_CATEGORY_ID)
                .name("New Category")
                .description("New Category Description")
                .build();
    }

    @Test
    void createProduct_shouldReturnProductId() {
        when(productMapper.fromCreateProductRequestToProduct(createProductRequest)).thenReturn(newProduct);
        when(productRepository.save(newProduct)).thenReturn(savedProduct);

        Long productId = productService.createProduct(createProductRequest);

        assertEquals(PRODUCT_ID, productId);
        verify(productRepository, times(1)).save(newProduct);
        verify(productMapper, times(1)).fromCreateProductRequestToProduct(createProductRequest);
    }

    @Test
    void getProductById_whenExists_shouldReturnProductResponse() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(savedProduct));
        when(productMapper.fromProductToProductResponse(savedProduct)).thenReturn(productResponse);

        ProductResponse foundProduct = productService.getProductById(PRODUCT_ID);

        assertEquals(productResponse, foundProduct);
        verify(productRepository, times(1)).findById(PRODUCT_ID);
        verify(productMapper, times(1)).fromProductToProductResponse(savedProduct);
    }

    @Test
    void getProductById_whenNotExists_shouldThrowException() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.getProductById(PRODUCT_ID));

        assertEquals(String.format("Product with id %s not found", PRODUCT_ID), exception.getMessage());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
        verify(productMapper, never()).fromProductToProductResponse(savedProduct);
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() {
        List<Product> products = List.of(savedProduct);
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.fromProductToProductResponse(savedProduct)).thenReturn(productResponse);

        List<ProductResponse> foundProducts = productService.getAllProducts();

        assertEquals(1, foundProducts.size());
        assertEquals(productResponse, foundProducts.getFirst());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).fromProductToProductResponse(savedProduct);
    }

    @Test
    void updateProduct_whenProductAndCategoryExist_shouldReturnProductId() {
        Product updatedProduct = Product.builder()
                .id(PRODUCT_ID)
                .name(updateProductRequest.name())
                .description(updateProductRequest.description())
                .price(updateProductRequest.price())
                .availableQuantity(updateProductRequest.availableQuantity())
                .productCategory(newProductCategory)
                .build();

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(savedProduct));
        when(productCategoryRepository.findById(NEW_PRODUCT_CATEGORY_ID)).thenReturn(Optional.of(newProductCategory));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Long resultId = productService.updateProduct(updateProductRequest);

        assertEquals(PRODUCT_ID, resultId);
        verify(productRepository).findById(PRODUCT_ID);
        verify(productCategoryRepository).findById(NEW_PRODUCT_CATEGORY_ID);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_whenProductNotFound_shouldThrowException() {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest(
                PRODUCT_ID,
                "Updated Name",
                "Updated Description",
                5,
                BigDecimal.valueOf(20.0),
                1L
        );

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.updateProduct(updateProductRequest));

        assertEquals(String.format("Product with id %s not found", PRODUCT_ID), exception.getMessage());
        verify(productRepository).findById(PRODUCT_ID);
        verify(productCategoryRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_whenCategoryNotFound_shouldThrowException() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(savedProduct));
        when(productCategoryRepository.findById(NEW_PRODUCT_CATEGORY_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.updateProduct(updateProductRequest));

        assertEquals(String.format("Product category with id %s not found", NEW_PRODUCT_CATEGORY_ID), exception.getMessage());
        verify(productRepository).findById(PRODUCT_ID);
        verify(productCategoryRepository).findById(NEW_PRODUCT_CATEGORY_ID);
        verify(productRepository, never()).save(any());
    }

    @Test
    void purchaseProducts_whenProductsExistAndHaveStock_shouldReturnPurchasedProducts() {
        ProductPurchaseRequest request1 = new ProductPurchaseRequest(1L, 1);
        ProductPurchaseRequest request2 = new ProductPurchaseRequest(2L, 2);
        List<ProductPurchaseRequest> requests = List.of(request1, request2);

        Product product1 = Product.builder()
                .id(1L)
                .availableQuantity(5)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .availableQuantity(5)
                .build();

        ProductPurchaseResponse response1 = new ProductPurchaseResponse(1L, "test", "test", BigDecimal.ONE, 1);
        ProductPurchaseResponse response2 = new ProductPurchaseResponse(2L, "test", "test", BigDecimal.TWO, 2);

        when(productRepository.findAllByIdInOrderById(any())).thenReturn(List.of(product1, product2));
        when(productMapper.fromProductToProductPurchaseResponse(product1, 1)).thenReturn(response1);
        when(productMapper.fromProductToProductPurchaseResponse(product2, 2)).thenReturn(response2);

        List<ProductPurchaseResponse> result = productService.purchaseProducts(requests);

        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
        verify(productRepository).findAllByIdInOrderById(any());
        verify(productRepository, times(2)).save(any());
    }

    @Test
    void purchaseProducts_whenProductNotFound_shouldThrowException() {
        List<ProductPurchaseRequest> requests = List.of(
                new ProductPurchaseRequest(1L, 1)
        );

        when(productRepository.findAllByIdInOrderById(any())).thenReturn(List.of());

        ProductPurchaseException exception = assertThrows(ProductPurchaseException.class,
                () -> productService.purchaseProducts(requests));

        assertEquals("One or more products does not exist", exception.getMessage());
        verify(productRepository).findAllByIdInOrderById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void purchaseProducts_whenInsufficientStock_shouldThrowException() {
        ProductPurchaseRequest request = new ProductPurchaseRequest(1L, 10);
        List<ProductPurchaseRequest> requests = List.of(request);

        Product product = Product.builder()
                .id(1L)
                .availableQuantity(5)
                .build();

        when(productRepository.findAllByIdInOrderById(any())).thenReturn(List.of(product));

        ProductPurchaseException exception = assertThrows(ProductPurchaseException.class,
                () -> productService.purchaseProducts(requests));

        assertEquals("One or more products do not have enough stock", exception.getMessage());
        verify(productRepository).findAllByIdInOrderById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProductById_shouldCallRepositoryDeleteMethod() {
        productService.deleteProductById(PRODUCT_ID);

        verify(productRepository).deleteById(PRODUCT_ID);
    }
}
