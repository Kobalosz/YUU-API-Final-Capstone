package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.OrderLineItem;
import org.yearup.models.Orders;
import org.yearup.models.ShoppingCart;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.math.BigDecimal; //I tried using BigDecimal because Gregor said it provided better financial accuracy, but I couldn't get it crackin
import java.time.LocalDateTime;

@Service
public class OrderService
{
    // I made these final because I'd read that doing so prevents them from being changed in prod
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ShoppingCartService  shoppingCartService;

    public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, ShoppingCartService shoppingCartService)
    {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Transactional
    public Orders checkout(int userId)
    {
       ShoppingCart cart = shoppingCartService.getByUserId(userId);

       Orders order = new Orders();
       order.setUserId(userId);
       order.setDate(LocalDateTime.now());
       orderRepository.save(order);

       cart.getItems().values().forEach(item -> {
           OrderLineItem orderLineItem = new OrderLineItem();
           orderLineItem.setOrderId(order.getOrderId());
           orderLineItem.setProductId(item.getProductId());
           orderLineItem.setQuantity(item.getQuantity());
           orderLineItem.setSalesPrices(item.getProduct().getPrice());
           orderLineItem.setDiscount(item.getProduct().getPrice());
           orderLineItemRepository.save(orderLineItem);
       });
       shoppingCartService.clearCart(userId);
       return order;
    }
}
