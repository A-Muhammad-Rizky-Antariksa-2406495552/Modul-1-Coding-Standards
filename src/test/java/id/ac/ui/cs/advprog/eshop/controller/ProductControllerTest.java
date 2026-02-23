package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void homeReturnsProductListView() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/product/"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));

        verify(productService).findAll();
    }

    @Test
    void createProductPageReturnsCreateView() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPostWithValidDataRedirects() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "Sampo Cap Bambang")
                        .param("productQuantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService).create(any(Product.class));
    }

    @Test
    void createProductPostWithEmptyNameReturnsCreateView() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "")
                        .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("error"));

        verify(productService, never()).create(any());
    }

    @Test
    void createProductPostWithNullNameReturnsCreateView() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("error"));

        verify(productService, never()).create(any());
    }

    @Test
    void createProductPostWithNegativeQuantityReturnsCreateView() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "Sampo Cap Bambang")
                        .param("productQuantity", "-5"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("error"));

        verify(productService, never()).create(any());
    }

    @Test
    void productListPageReturnsProductListView() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productService.findAll()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void editProductPageWithExistingProductReturnsEditView() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productService.findById("1")).thenReturn(product);

        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void editProductPageWithNonExistentProductRedirects() throws Exception {
        when(productService.findById("999")).thenReturn(null);

        mockMvc.perform(get("/product/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void editProductPostWithValidDataRedirects() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Sampo Cap Bambang")
                        .param("productQuantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService).update(eq("1"), any(Product.class));
    }

    @Test
    void editProductPostWithEmptyNameReturnsEditView() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "")
                        .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("product"));

        verify(productService, never()).update(any(), any());
    }

    @Test
    void editProductPostWithNegativeQuantityReturnsEditView() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Sampo Cap Bambang")
                        .param("productQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("error"));

        verify(productService, never()).update(any(), any());
    }

    @Test
    void deleteProductWithExistingProductDeletesAndRedirects() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productService.findById("1")).thenReturn(product);

        mockMvc.perform(post("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService).delete("1");
    }

    @Test
    void deleteProductWithNonExistentProductRedirectsWithoutDeleting() throws Exception {
        when(productService.findById("1")).thenReturn(null);

        mockMvc.perform(post("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService, never()).delete(any());
    }
}