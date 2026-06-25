package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        ShoppingCart cart = new ShoppingCart();
        for(CartItem item : shoppingCartRepository.findByUserId(userId)){
            Product product = productService.getById(item.getProductId());
            if(product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");

            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(item.getQuantity());
            cart.add(shoppingCartItem);
        }
        return cart;
    }
    // add additional methods here

    public ShoppingCart addProduct(int userId, int productId)
    {
        CartItem existing = shoppingCartRepository
                .findByUserIdAndProductId(userId, productId).orElse(null);
        if(existing == null) {
                existing.setQuantity(existing.getQuantity() + 1);
            shoppingCartRepository.save(existing);
        }else{


            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            shoppingCartRepository.save(newItem);
        }
        return getByUserId(userId);

    }

    public ShoppingCart updateProduct(int userId, int productId, int quantity)
    {
        CartItem existing = shoppingCartRepository
                .findByUserIdAndProductId(userId, productId).orElse(null);

        if(existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            shoppingCartRepository.save(existing);
        }
        return getByUserId(userId);
    }

    @Transactional
    public ShoppingCart clearCart(int userId){
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }

    @Transactional
    public ShoppingCart removeProduct(int userId, int productId, int quantity)
    {
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }

}
