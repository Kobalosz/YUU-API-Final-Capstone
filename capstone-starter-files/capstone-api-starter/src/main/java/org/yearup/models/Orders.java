package org.yearup.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "order_id") private Integer orderId;
    @Column(name = "user_id") private Integer userId;
    @Column(name = "date")  private LocalDateTime date;
    @Column(name = "address")   private String address;
    @Column(name = "city") private String city;
    @Column(name = "state") private String state;
    @Column(name = "zip")  private String zip;
    @Column(name = "shipping_amount") private BigDecimal shippingAmount;

    public Orders() {}

    public Orders(Integer userId, LocalDateTime date, String address, String city, String state, String zip, BigDecimal shippingAmount)
    {
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shippingAmount = shippingAmount;
    }

    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId)
    {
        this.orderId = orderId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip)
    {
        this.zip = zip;
    }
    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }
    public void setShippingAmount(BigDecimal shippingAmount)
    {
        this.shippingAmount = shippingAmount;
    }



}
