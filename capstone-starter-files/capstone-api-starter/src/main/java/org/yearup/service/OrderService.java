package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.OrderLineItem;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService
{
    // I made these final because I'd read that doing so prevents them from being changed in prod
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ShoppingCartService  shoppingCartService;
    private final ProfileService profileService;

    public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, ShoppingCartService shoppingCartService, ProfileService profileService)
    {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.profileService = profileService;
    }

    @Transactional
    public Order checkout(int userId)
    {
       ShoppingCart cart = shoppingCartService.getByUserId(userId);
       if(cart.getItems().isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");

       Profile profile = profileService.GetProfileById(userId);

       Order order = new Order();
       order.setUserId(userId);
       order.setDate(LocalDateTime.now());
       order.setAddress(profile != null ? profile.getAddress() : "");
       order.setCity(profile != null ? profile.getCity() : "");
       order.setState(profile != null ? profile.getState() : "");
       order.setZip(profile != null ? profile.getZip() : "");
       order.setShippingAmount(BigDecimal.ZERO);
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
