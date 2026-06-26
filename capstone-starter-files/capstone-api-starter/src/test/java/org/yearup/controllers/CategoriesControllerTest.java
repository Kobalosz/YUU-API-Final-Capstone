package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoriesControllerTest
{
    private CategoryService categoryService;
    private ProductService productService;
    private CategoriesController categoriesController;

    @BeforeEach
    void setUp()
    {
        categoryService = mock(CategoryService.class);
        productService = mock(ProductService.class);
        categoriesController = new CategoriesController(categoryService, productService);
    }

    @Test
    void getAll_returnsAllCategories()
    {
        // arrange
        when(categoryService.getAllCategories()).thenReturn(List.of(
                new Category(1, "Electronics", "Gadgets"),
                new Category(2, "Fashion", "Clothing")
        ));

        // act
        List<Category> result = categoriesController.getAll();

        // assert
        assertThat(result).hasSize(2);
    }

    @Test
    void getById_returnsCategory_whenFound()
    {
        // arrange
        when(categoryService.getById(1)).thenReturn(new Category(1, "Electronics", "Gadgets"));

        // act
        Category result = categoriesController.getById(1);

        // assert
        assertThat(result.getName()).isEqualTo("Electronics");
    }

    @Test
    void getById_throwsNotFound_whenMissing()
    {
        // arrange
        when(categoryService.getById(99)).thenReturn(null);

        // act + assert got lazy
        assertThrows(ResponseStatusException.class, () -> categoriesController.getById(99));
    }

    @Test
    void getProductsById_returnsProducts()
    {
        // arrange
        when(productService.listByCategoryId(1)).thenReturn(List.of(new Product()));

        // act
        List<Product> result = categoriesController.getProductsById(1);

        // assert
        assertThat(result).hasSize(1);
    }

    @Test
    void addCategory_returnsCreated()
    {
        // arrange
        Category category = new Category(0, "Toys", "Fun stuff");
        when(categoryService.create(category)).thenReturn(new Category(5, "Toys", "Fun stuff"));

        // act
        ResponseEntity<Category> result = categoriesController.addCategory(category);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getCategoryId()).isEqualTo(5);
    }

    @Test
    void updateCategory_throwsNotFound_whenMissing()
    {
        // arrange
        when(categoryService.getById(99)).thenReturn(null);

        // act + assert
        assertThrows(ResponseStatusException.class, () -> categoriesController.updateCategory(99, new Category()));
    }

    @Test
    void deleteCategory_returnsNoContent_whenFound()
    {
        // arrange
        when(categoryService.getById(1)).thenReturn(new Category(1, "Electronics", "Gadgets"));

        // act
        ResponseEntity<Void> result = categoriesController.deleteCategory(1);

        // assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(categoryService).delete(1);
    }
}
