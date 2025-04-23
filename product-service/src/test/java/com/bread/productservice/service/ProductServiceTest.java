package com.bread.productservice.service;

import com.bread.productservice.dto.PagedResponseDTO;
import com.bread.productservice.model.Product;
import com.bread.productservice.model.ProductType;
import com.bread.productservice.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProduct = new Product("1", "Croissant", "Pan hojaldrado", 25.0, 10, "http://imgUrl.jpg", ProductType.GLUTEN);
    }

    @Test
    void shouldGetAllProductsPagedSorted() {
        List<Product> products = List.of(sampleProduct);
        Page<Product> page = new PageImpl<>(products);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        PagedResponseDTO<Product> result = productService.getAllProductsPagedSorted(0, 5, "DESC");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(products, result.getContent());
    }

    @Test
    void shouldSearchProductsByName() {
        List<Product> products = List.of(sampleProduct);
        when(productRepository.findByNameContainingIgnoreCase(eq("Croissant"), any(Pageable.class)))
                .thenReturn(products);

        List<Product> result = productService.searchProductsByName("Croissant", 0, 5);
        assertEquals(1, result.size());
        assertEquals("Croissant", result.get(0).getName());
    }

    @Test
    void shouldFilterProductsByType() {
        List<Product> products = List.of(sampleProduct);
        when(productRepository.findByType(eq(ProductType.GLUTEN), any(Pageable.class))).thenReturn(products);

        List<Product> result = productService.filterProducts(ProductType.GLUTEN, null, null, 0, 5);
        assertEquals(1, result.size());
    }

    @Test
    void shouldFilterProductsByPriceRange() {
        List<Product> products = List.of(sampleProduct);
        when(productRepository.findByPriceBetween(eq(20.0), eq(30.0), any(Pageable.class))).thenReturn(products);

        List<Product> result = productService.filterProducts(null, 20.0, 30.0, 0, 5);
        assertEquals(1, result.size());
    }

    @Test
    void shouldFilterProductsByTypeAndPrice() {
        List<Product> products = List.of(sampleProduct);
        when(productRepository.findByTypeAndPriceBetween(eq(ProductType.GLUTEN), eq(20.0), eq(30.0), any(Pageable.class)))
                .thenReturn(products);

        List<Product> result = productService.filterProducts(ProductType.GLUTEN, 20.0, 30.0, 0, 5);
        assertEquals(1, result.size());
    }

    @Test
    void shouldCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        Product created = productService.createProduct(sampleProduct);
        assertEquals("Croissant", created.getName());
    }

    @Test
    void shouldUpdateProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product update = new Product();
        update.setName("Nuevo Croissant");
        update.setDescription("Actualizado");
        update.setPrice(30.0);
        update.setQuantity(15);
        update.setImgUrl("http://nueva.img");
        update.setType(ProductType.VEGAN);

        Product result = productService.updateProduct("1", update);

        assertEquals("Nuevo Croissant", result.getName());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingProduct() {
        when(productRepository.findById("2")).thenReturn(Optional.empty());
        Product dummy = new Product();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct("2", dummy);
        });

        assertEquals("Product not found with id: 2", exception.getMessage());
    }

    @Test
    void shouldDeleteProduct() {
        assertDoesNotThrow(() -> productService.deleteProduct("1"));
        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void shouldGetProductById() {
        when(productRepository.findById("1")).thenReturn(Optional.of(sampleProduct));
        Optional<Product> result = productService.getProductById("1");
        assertTrue(result.isPresent());
        assertEquals("Croissant", result.get().getName());
    }

    @Test
    void shouldGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));
        List<Product> result = productService.getAllProducts();
        assertEquals(1, result.size());
    }
}
