package org.yearup.models;

import jakarta.persistence.*;



@Entity
@Table(name = "order_line_items")
public class OrderLineItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private int orderLineItemId;

    @Column(name = "order_id") private int orderId;
    @Column(name = "product_id") private int productId;
    @Column(name = "sales_prices") private double salesPrices;
    @Column(name = "quantity")  private int quantity;
    @Column(name = "discount") private double discount;

    public int getOrderLineItemId() {
        return orderLineItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getDiscount() {
        return discount;
    }

    public double getSalesPrices() {
        return salesPrices;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setOrderLineItemId(int orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSalesPrices(double salesPrices) {
        this.salesPrices = salesPrices;
    }
}
