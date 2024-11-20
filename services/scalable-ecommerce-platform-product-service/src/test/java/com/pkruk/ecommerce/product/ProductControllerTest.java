package com.pkruk.ecommerce.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkruk.ecommerce.advice.GlobalControllerAdvice;
import com.pkruk.ecommerce.product.dto.CreateProductRequest;
import com.pkruk.ecommerce.product.dto.ProductResponse;
import com.pkruk.ecommerce.product.dto.UpdateProductRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {
        ProductController.class,
        GlobalControllerAdvice.class
})
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private static final Long PRODUCT_ID = 1L;
    private static final Long PRODUCT_CATEGORY_ID = 1L;
    private static final String PRODUCT_NAME = "Product Name";
    private static final String PRODUCT_DESCRIPTION = "Product Description";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.TEN;
    private static final Integer PRODUCT_QUANTITY = 1;
    private static final String BASE_URL = "/product";

    private CreateProductRequest createProductRequest;
    private UpdateProductRequest updateProductRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        createProductRequest = new CreateProductRequest(
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION,
                PRODUCT_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY_ID
        );

        updateProductRequest = new UpdateProductRequest(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION,
                PRODUCT_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY_ID
        );

        productResponse = new ProductResponse(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION,
                PRODUCT_QUANTITY,
                PRODUCT_PRICE,
                PRODUCT_CATEGORY_ID,
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION
        );
    }

    @Test
    void addNewProduct_WhenValidRequest_ShouldReturnOkWithId() throws Exception {
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(PRODUCT_ID);

        mockMvc.perform(post(BASE_URL + "/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(PRODUCT_ID.toString()));

        verify(productService).createProduct(any(CreateProductRequest.class));
    }

    @Test
    void addNewProduct_WhenInvalidRequest_ShouldReturnBadRequestWithErrors() throws Exception {
        CreateProductRequest invalidRequest = new CreateProductRequest("",
                "",
                -1,
                BigDecimal.valueOf(-1L),
                null
                );

        mockMvc.perform(post(BASE_URL + "/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("Product name cannot be blank"))
                .andExpect(jsonPath("$.errors.description").value("Product description cannot be blank"))
                .andExpect(jsonPath("$.errors.availableQuantity").value("Product available quantity must be a positive value"))
                .andExpect(jsonPath("$.errors.price").value("Product price must be a positive value"))
                .andExpect(jsonPath("$.errors.categoryId").value("Product category is required"));

        verify(productService, never()).createProduct(any(CreateProductRequest.class));
    }

    @Test
    void updateProduct_WhenValidRequest_ShouldReturnOkWithId() throws Exception {
        when(productService.updateProduct(any(UpdateProductRequest.class)))
                .thenReturn(PRODUCT_ID);

        mockMvc.perform(put(BASE_URL + "/updateProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(PRODUCT_ID.toString()));

        verify(productService).updateProduct(any(UpdateProductRequest.class));
    }

    @Test
    void updateProduct_WhenCategoryNotFound_ShouldReturnBadRequestWithMessage() throws Exception {
        String errorMessage = "Category not found";
        when(productService.updateProduct(any(UpdateProductRequest.class)))
                .thenThrow(new EntityNotFoundException(errorMessage));

        mockMvc.perform(put(BASE_URL + "/updateProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(productService).updateProduct(any(UpdateProductRequest.class));
    }

    @Test
    void getProduct_WhenExists_ShouldReturnCategory() throws Exception {
        when(productService.getProductById(PRODUCT_ID))
                .thenReturn(productResponse);

        mockMvc.perform(get(BASE_URL + "/{product-category-id}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.description").value(PRODUCT_DESCRIPTION));

        verify(productService).getProductById(PRODUCT_ID);
    }

    @Test
    void getProduct_WhenNotExists_ShouldReturnBadRequestWithMessage() throws Exception {
        String errorMessage = String.format("Product with id %s not found", PRODUCT_ID);
        when(productService.getProductById(PRODUCT_ID))
                .thenThrow(new EntityNotFoundException(errorMessage));

        mockMvc.perform(get(BASE_URL + "/{product-category-id}", PRODUCT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(productService).getProductById(PRODUCT_ID);
    }

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        List<ProductResponse> productResponses = List.of(productResponse);
        when(productService.getAllProducts()).thenReturn(productResponses);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(PRODUCT_ID))
                .andExpect(jsonPath("$[0].name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[0].description").value(PRODUCT_DESCRIPTION));

        verify(productService).getAllProducts();
    }

    @Test
    void deleteProduct_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{product-id}", PRODUCT_ID))
                .andExpect(status().isNoContent());

        verify(productService).deleteProductById(PRODUCT_ID);
    }

    @Test
    void deleteProduct_WhenNotExists_ShouldReturnBadRequestWithMessage() throws Exception {
        String errorMessage = String.format("Product with id %s not found", PRODUCT_ID);
        doThrow(new EntityNotFoundException(errorMessage))
                .when(productService).deleteProductById(PRODUCT_ID);

        mockMvc.perform(delete(BASE_URL + "/{product-id}", PRODUCT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(productService).deleteProductById(PRODUCT_ID);
    }
}
