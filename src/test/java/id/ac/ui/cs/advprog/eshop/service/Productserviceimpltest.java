package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setProductId("test-id-123");
        sampleProduct.setProductName("Sampo Cap Bambang");
        sampleProduct.setProductQuantity(100);
    }

    @Test
    void testCreateProductDelegatesToRepository() {
        when(productRepository.create(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.create(sampleProduct);

        assertEquals(sampleProduct, result);
        verify(productRepository).create(sampleProduct);
    }

    @Test
    void testFindAllReturnsProductList() {
        Iterator<Product> iterator = List.of(sampleProduct).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> products = productService.findAll();

        assertEquals(1, products.size());
        assertEquals(sampleProduct, products.get(0));
        verify(productRepository).findAll();
    }

    @Test
    void testFindAllReturnsEmptyListWhenNoProducts() {
        Iterator<Product> emptyIterator = Collections.emptyIterator();
        when(productRepository.findAll()).thenReturn(emptyIterator);

        List<Product> products = productService.findAll();

        assertTrue(products.isEmpty());
    }

    @Test
    void testFindAllReturnsMultipleProducts() {
        Product secondProduct = new Product();
        secondProduct.setProductId("test-id-456");
        secondProduct.setProductName("Sampo Cap Usep");
        secondProduct.setProductQuantity(50);

        Iterator<Product> iterator = List.of(sampleProduct, secondProduct).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> products = productService.findAll();

        assertEquals(2, products.size());
    }

    @Test
    void testFindByIdReturnsProduct() {
        when(productRepository.findById("test-id-123")).thenReturn(sampleProduct);

        Product result = productService.findById("test-id-123");

        assertNotNull(result);
        assertEquals("Sampo Cap Bambang", result.getProductName());
        verify(productRepository).findById("test-id-123");
    }

    @Test
    void testFindByIdReturnsNullWhenNotFound() {
        when(productRepository.findById("nonexistent")).thenReturn(null);

        Product result = productService.findById("nonexistent");

        assertNull(result);
    }

    @Test
    void testUpdateDelegatesToRepository() {
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Sampo Cap Bambang V2");
        updatedProduct.setProductQuantity(200);

        when(productRepository.update("test-id-123", updatedProduct))
                .thenReturn(updatedProduct);

        Product result = productService.update("test-id-123", updatedProduct);

        assertNotNull(result);
        assertEquals("Sampo Cap Bambang V2", result.getProductName());
        verify(productRepository).update("test-id-123", updatedProduct);
    }

    @Test
    void testUpdateReturnsNullWhenNotFound() {
        Product updatedProduct = new Product();
        when(productRepository.update("nonexistent", updatedProduct)).thenReturn(null);

        Product result = productService.update("nonexistent", updatedProduct);

        assertNull(result);
    }

    @Test
    void testDeleteDelegatesToRepository() {
        productService.delete("test-id-123");

        verify(productRepository).delete("test-id-123");
    }
}