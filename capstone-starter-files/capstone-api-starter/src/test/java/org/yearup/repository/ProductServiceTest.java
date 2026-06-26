package org.yearup.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Product;
import org.yearup.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {
    private ProductService productService;
    private ProductRepository productRepository;


    @BeforeEach
    void setUp()
    {
//        This mock() method isn't anything too special, it just allows me to simulate whatever object or interface is passed into it
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

//    I'm going to test and make sure that I can get all the products with no filters,
//    getting products with filters,
//    and updating an existing product
//    To do that I'm, going to create a very simple helper function to build the products because I'm lazy

    private Product makeProduct(int id, String name, double price, boolean featured) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setFeatured(featured);
        return product;
    }

    @Test
    void search_WithNo_Filters_returnsAllProducts() {
//        My arrange
        List<Product> allProducts = List.of(
                makeProduct(1, "dogFood", 67.67, false),
                makeProduct(2, "Pizza Cutter", 17.38, true),
                makeProduct(3, "QLED TV", 10000,false)
        );
        when(productRepository.findAll()).thenReturn(allProducts);

        // My act

        List<Product> result = productService.search(null,null,null,null,null);

        // My assert
        assertThat(result).hasSize(3);
    }

    @Test
    void search_With_Filters_returnsThoseProducts() {
        // My arrange
        List<Product> allProducts = List.of(
                makeProduct(1, "dogFood", 67.67, false),
                makeProduct(2, "Pizza Cutter", 17.38, true),
                makeProduct(3, "QLED TV", 10000, false)
        );
        when(productRepository.findAll()).thenReturn(allProducts);

        //My act
        List<Product> result = productService.search(null, null, null, null, true);

        // My assert
        assertThat(result).hasSize(1);
    }

    @Test
    void update_existingProduct() {
        // My arrange
        Product existingProduct = makeProduct(1, "dogFood", 67.67, false);
        existingProduct.setStock(29);
        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product newProduct = makeProduct(1, "dogFood", 67.67, false);
        newProduct.setStock(100);

        //My act
        productService.update(1, newProduct); // found the bug!

        // My assert
        assertThat(existingProduct.getStock()).isEqualTo(100);
    }


}
