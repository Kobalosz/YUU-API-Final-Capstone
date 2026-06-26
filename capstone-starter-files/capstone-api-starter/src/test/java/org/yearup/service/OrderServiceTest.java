package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Order;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest
{
    private OrderRepository orderRepository;
    private OrderLineItemRepository orderLineItemRepository;
    private ShoppingCartService shoppingCartService;
    private OrderService orderService;

    @BeforeEach
    void setUp()
    {
        orderRepository = mock(OrderRepository.class);
        orderLineItemRepository = mock(OrderLineItemRepository.class);
        shoppingCartService = mock(ShoppingCartService.class);
        orderService = new OrderService(orderRepository, orderLineItemRepository, shoppingCartService);
    }

    private ShoppingCartItem makeItem(int productId, double price, int quantity)
    {
        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(price);

        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }

    @Test
    void checkout_savesOrder_lineItems_andClearsCart()
    {
        // arrange
        ShoppingCart cart = new ShoppingCart();
        cart.add(makeItem(1, 10.0, 2));
        cart.add(makeItem(2, 25.0, 1));
        when(shoppingCartService.getByUserId(1)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(100);
            return order;
        });

        // act
        Order result = orderService.checkout(1);

        // assert
        assertThat(result.getUserId()).isEqualTo(1);
        assertThat(result.getOrderId()).isEqualTo(100);
        verify(orderRepository).save(any(Order.class));
        verify(orderLineItemRepository, times(2)).save(any());
        verify(shoppingCartService).clearCart(1);
    }
}
