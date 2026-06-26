package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryServiceTest
{
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp()
    {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getAllCategories_returnsAllCategories()
    {
        // arrange
        List<Category> categories = List.of(
                new Category(1, "Electronics", "Gadgets"),
                new Category(2, "Fashion", "Clothing")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        // act
        List<Category> result = categoryService.getAllCategories();

        // assert
        assertThat(result).hasSize(2);
    }

    @Test
    void getById_returnsTheCorrectCategory()
    {
        // arrange
        Category category = new Category(1, "Electronics", "Gadgets");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // act
        Category result = categoryService.getById(1);

        // assert
        assertThat(result.getName()).isEqualTo("Electronics");
    }

    @Test
    void update_existingCategory()
    {
        // arrange
        Category existing = new Category(1, "Electronics", "Gadgets");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        Category updated = new Category(1, "Tech", "New description");

        // act
        categoryService.update(1, updated);

        // assert
        assertThat(existing.getName()).isEqualTo("Tech");
        assertThat(existing.getDescription()).isEqualTo("New description");
    }

    @Test
    void delete_removesTheCategory()
    {
        // arrange
        Category existing = new Category(1, "Electronics", "Gadgets");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(existing));

        // act
        categoryService.delete(1);

        // assert
        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).delete(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Electronics");
    }
}
