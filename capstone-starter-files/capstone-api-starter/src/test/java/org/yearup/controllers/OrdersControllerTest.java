package org.yearup.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Order;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrdersControllerTest
{
    private OrderService orderService;
    private UserService userService;
    private OrdersController ordersController;

    @BeforeEach
    void setUp()
    {
        orderService = mock(OrderService.class);
        userService = mock(UserService.class);
        ordersController = new OrdersController(orderService, userService);
    }

    @Test
    void checkout_resolvesUserId_andReturnsOrder()
    {
        // arrange
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");
        when(userService.getIdByUsername("user")).thenReturn(1);

        Order order = new Order();
        order.setOrderId(50);
        order.setUserId(1);
        when(orderService.checkout(1)).thenReturn(order);

        // act
        Order result = ordersController.checkout(principal);

        // assert
        assertThat(result.getOrderId()).isEqualTo(50);
        verify(orderService).checkout(1);
    }
}
