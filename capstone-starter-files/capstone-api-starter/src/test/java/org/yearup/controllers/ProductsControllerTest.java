package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.service.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductsControllerTest
{
    private ProductService productService;
    private ProductsController productsController;

    @BeforeEach
    void setUp()
    {
        productService = mock(ProductService.class);
        productsController = new ProductsController(productService);
    }

    @Test
    void search_returnsMatchingProducts()
    {
        // arrange
        when(productService.search(null, null, null, null, null)).thenReturn(List.of(new Product(), new Product()));

        // act
        List<Product> result = productsController.search(null, null, null, null, null);

        // assert
        assertThat(result).hasSize(2);
    }

    @Test
    void getById_returnsProduct_whenFound()
    {
        // arrange
        Product product = new Product();
        product.setProductId(1);
        product.setName("Smartphone");
        when(productService.getById(1)).thenReturn(product);

        // act
        Product result = productsController.getById(1);

        // assert
        assertThat(result.getName()).isEqualTo("Smartphone");
    }

    @Test
    void getById_throwsNotFound_whenMissing()
    {
        // arrange
        when(productService.getById(99)).thenReturn(null);

        // act + assert
        assertThrows(ResponseStatusException.class, () -> productsController.getById(99));
    }

    @Test
    void addProduct_returnsCreated()
    {
        // arrange
        Product product = new Product();
        product.setName("New Product");
        Product saved = new Product();
        saved.setProductId(10);
        when(productService.create(product)).thenReturn(saved);

        // act
        ResponseEntity<Product> result = productsController.addProduct(product);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getProductId()).isEqualTo(10);
    }

    @Test
    void updateProduct_throwsNotFound_whenMissing()
    {
        // arrange
        when(productService.getById(99)).thenReturn(null);

        // act + assert
        assertThrows(ResponseStatusException.class, () -> productsController.updateProduct(99, new Product()));
    }

    @Test
    void deleteProduct_returnsNoContent_whenFound()
    {
        // arrange
        Product product = new Product();
        product.setProductId(1);
        when(productService.getById(1)).thenReturn(product);

        // act
        ResponseEntity<Void> result = productsController.deleteProduct(1);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(productService).delete(1);
    }
}
