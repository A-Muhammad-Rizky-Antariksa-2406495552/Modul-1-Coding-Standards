package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void editProduct_success() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        when(productService.findById("1")).thenReturn(product);

        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Sampo Cap Bambang")
                        .param("productQuantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService).update(eq("1"), any(Product.class));
    }

    @Test
    void editProduct_emptyName() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "")
                        .param("productQuantity", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"));

        verify(productService, never()).update(any(), any());
    }

    @Test
    void editProduct_negativeQuantity() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Sampo Cap Bambang")
                        .param("productQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"));

        verify(productService, never()).update(any(), any());
    }

    @Test
    void deleteProduct_success() throws Exception {
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
    void deleteProduct_notFound() throws Exception {
        when(productService.findById("1")).thenReturn(null);

        mockMvc.perform(post("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService, never()).delete(any());
    }
}
