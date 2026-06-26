package org.yearup.repository;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.yearup.models.CartItem;
import org.yearup.service.ProductService;
import org.yearup.service.ShoppingCartService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest
{
    private ShoppingCartRepository shoppingCartRepository;
    private ProductService productService;
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp()
    {
        shoppingCartRepository = mock(ShoppingCartRepository.class);
        productService = mock(ProductService.class);
        shoppingCartService = new ShoppingCartService(shoppingCartRepository, productService);
    }

    @Test
    void addProduct_WhenNotInCart_insertsJustOne()
    {
//        My arrange
        when(shoppingCartRepository.findByUserIdAndProductId(1,15)).thenReturn(Optional.empty());
        when(shoppingCartRepository.findByUserId(1)).thenReturn(List.of());

//        My act
        shoppingCartService.addProduct(1,15);

//        My assert
//        I was researching a way to see what I'm actually passing to methods and found this cool ArgumentCaptor
        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        verify(shoppingCartRepository).save(captor.capture());

        CartItem savedValue = captor.getValue();
        assertThat(savedValue.getUserId()).isEqualTo(1);
        assertThat(savedValue.getProductId()).isEqualTo(15);
        assertThat(savedValue.getQuantity()).isEqualTo(1);
    }

    @Test
    void addProduct_WhenInCart_insertsJustOne()
        {
//            My arrange
            CartItem existingProduct = new CartItem();
            existingProduct.setUserId(1);
            existingProduct.setProductId(1);
            existingProduct.setQuantity(1);
            when(shoppingCartRepository.findByUserIdAndProductId(1,15)).thenReturn(Optional.of(existingProduct));
            when(shoppingCartRepository.findByUserId(1)).thenReturn(List.of());

//            My act
            shoppingCartService.addProduct(1,15);

//            My assert
            assertThat(existingProduct.getQuantity()).isEqualTo(2);
            verify(shoppingCartRepository).save(existingProduct);
        }






}
