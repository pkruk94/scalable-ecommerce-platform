package com.pkruk.ecommerce.product_category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkruk.ecommerce.advice.GlobalControllerAdvice;
import com.pkruk.ecommerce.product_category.dto.NewProductCategoryRequest;
import com.pkruk.ecommerce.product_category.dto.ProductCategoryResponse;
import com.pkruk.ecommerce.product_category.dto.UpdateProductCategoryRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        ProductCategoryController.class,
        GlobalControllerAdvice.class
})
class ProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductCategoryService productCategoryService;

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Test Category";
    private static final String CATEGORY_DESCRIPTION = "Test Description";
    private static final String BASE_URL = "/product/category";

    private NewProductCategoryRequest newProductCategoryRequest;
    private UpdateProductCategoryRequest updateProductCategoryRequest;
    private ProductCategoryResponse productCategoryResponse;

    @BeforeEach
    void setUp() {
        newProductCategoryRequest = new NewProductCategoryRequest(
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION
        );

        updateProductCategoryRequest = new UpdateProductCategoryRequest(
                CATEGORY_ID,
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION
        );

        productCategoryResponse = new ProductCategoryResponse(
                CATEGORY_ID,
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION
        );
    }

    @Test
    void addNewCategory_WhenValidRequest_ShouldReturnOkWithId() throws Exception {
        // given
        when(productCategoryService.addNewProductCategory(any(NewProductCategoryRequest.class)))
                .thenReturn(CATEGORY_ID);

        // when & then
        mockMvc.perform(post(BASE_URL + "/addNewCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProductCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(CATEGORY_ID.toString()));

        verify(productCategoryService).addNewProductCategory(any(NewProductCategoryRequest.class));
    }

    @Test
    void addNewCategory_WhenInvalidRequest_ShouldReturnBadRequestWithErrors() throws Exception {
        // given
        NewProductCategoryRequest invalidRequest = new NewProductCategoryRequest("", "");

        // when & then
        mockMvc.perform(post(BASE_URL + "/addNewCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("Product category name is required"))
                .andExpect(jsonPath("$.errors.description").value("Product category description is required"));

        verify(productCategoryService, never()).addNewProductCategory(any());
    }

    @Test
    void updateProductCategory_WhenValidRequest_ShouldReturnOkWithId() throws Exception {
        // given
        when(productCategoryService.updateProductCategory(any(UpdateProductCategoryRequest.class)))
                .thenReturn(CATEGORY_ID);

        // when & then
        mockMvc.perform(put(BASE_URL + "/updateProductCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(CATEGORY_ID.toString()));

        verify(productCategoryService).updateProductCategory(any(UpdateProductCategoryRequest.class));
    }

    @Test
    void updateProductCategory_WhenCategoryNotFound_ShouldReturnBadRequestWithMessage() throws Exception {
        // given
        String errorMessage = "Category not found";
        when(productCategoryService.updateProductCategory(any(UpdateProductCategoryRequest.class)))
                .thenThrow(new EntityNotFoundException(errorMessage));

        // when & then
        mockMvc.perform(put(BASE_URL + "/updateProductCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductCategoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(productCategoryService).updateProductCategory(any(UpdateProductCategoryRequest.class));
    }

    @Test
    void getProductCategory_WhenExists_ShouldReturnCategory() throws Exception {
        // given
        when(productCategoryService.getProductCategoryById(CATEGORY_ID))
                .thenReturn(productCategoryResponse);

        // when & then
        mockMvc.perform(get(BASE_URL + "/{product-category-id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.string").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(CATEGORY_DESCRIPTION));

        verify(productCategoryService).getProductCategoryById(CATEGORY_ID);
    }

    @Test
    void getProductCategory_WhenNotExists_ShouldReturnBadRequestWithMessage() throws Exception {
        // given
        String errorMessage = String.format("Product category with id %s not found", CATEGORY_ID);
        when(productCategoryService.getProductCategoryById(CATEGORY_ID))
                .thenThrow(new EntityNotFoundException(errorMessage));

        // when & then
        mockMvc.perform(get(BASE_URL + "/{product-category-id}", CATEGORY_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(productCategoryService).getProductCategoryById(CATEGORY_ID);
    }

    @Test
    void getAllProductCategory_ShouldReturnList() throws Exception {
        // given
        List<ProductCategoryResponse> categories = List.of(productCategoryResponse);
        when(productCategoryService.getAllProductCategories()).thenReturn(categories);

        // when & then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[0].string").value(CATEGORY_NAME))
                .andExpect(jsonPath("$[0].description").value(CATEGORY_DESCRIPTION));

        verify(productCategoryService).getAllProductCategories();
    }

    @Test
    void deleteProductCategory_ShouldReturnOk() throws Exception {
        // when & then
        mockMvc.perform(delete(BASE_URL + "/{product-category-id}", CATEGORY_ID))
                .andExpect(status().isNoContent());

        verify(productCategoryService).deleteProductCategoryById(CATEGORY_ID);
    }

    @Test
    void deleteProductCategory_WhenNotExists_ShouldReturnBadRequestWithMessage() throws Exception {
        // given
        String errorMessage = String.format("Product category with id %s not found", CATEGORY_ID);
        doThrow(new EntityNotFoundException(errorMessage))
                .when(productCategoryService).deleteProductCategoryById(CATEGORY_ID);

        // when & then
        mockMvc.perform(delete(BASE_URL + "/{product-category-id}", CATEGORY_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(productCategoryService).deleteProductCategoryById(CATEGORY_ID);
    }
}
