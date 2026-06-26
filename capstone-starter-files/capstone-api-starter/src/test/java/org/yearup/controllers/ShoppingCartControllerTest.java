package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ProductService;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShoppingCartControllerTest
{
    private ShoppingCartService shoppingCartService;
    private UserService userService;
    private ProductService productService;
    private ShoppingCartController shoppingCartController;

    @BeforeEach
    void setUp()
    {
        shoppingCartService = mock(ShoppingCartService.class);
        userService = mock(UserService.class);
        productService = mock(ProductService.class);
        shoppingCartController = new ShoppingCartController(shoppingCartService, userService, productService);
    }

    private Principal loggedInUser()
    {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");
        when(userService.getByUserName("user")).thenReturn(new User(1, "user", "password", "ROLE_USER"));
        return principal;
    }

    private ShoppingCart cartWithOneItem()
    {
        Product product = new Product();
        product.setProductId(15);
        product.setPrice(10.0);
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        item.setQuantity(1);
        ShoppingCart cart = new ShoppingCart();
        cart.add(item);
        return cart;
    }

    @Test
    void getCart_returnsCartForLoggedInUser()
    {
        // arrange
        Principal principal = loggedInUser();
        when(shoppingCartService.getByUserId(1)).thenReturn(cartWithOneItem());

        // act
        ShoppingCart result = shoppingCartController.getCart(principal);

        // assert
        assertThat(result.contains(15)).isTrue();
    }

    @Test
    void addProduct_addsItemToCart()
    {
        // arrange
        Principal principal = loggedInUser();
        when(shoppingCartService.addProduct(1, 15)).thenReturn(cartWithOneItem());

        // act
        ShoppingCart result = shoppingCartController.addProduct(principal, 15);

        // assert
        assertThat(result.contains(15)).isTrue();
        verify(shoppingCartService).addProduct(1, 15);
    }

    @Test
    void updateCart_updatesItemQuantity()
    {
        // arrange
        Principal principal = loggedInUser();
        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setQuantity(3);
        when(shoppingCartService.updateProduct(1, 15, 3)).thenReturn(cartWithOneItem());

        // act
        ShoppingCart result = shoppingCartController.updateCart(15, cartItem, principal);

        // assert
        assertThat(result.contains(15)).isTrue();
        verify(shoppingCartService).updateProduct(1, 15, 3);
    }

    @Test
    void deleteCart_clearsCart()
    {
        // arrange
        Principal principal = loggedInUser();
        when(shoppingCartService.clearCart(1)).thenReturn(new ShoppingCart());

        // act
        ShoppingCart result = shoppingCartController.deleteCart(principal);

        // assert
        assertThat(result.getItems()).isEmpty();
        verify(shoppingCartService).clearCart(1);
    }
}
