package com.example.common;

public class Order {
    private String orderId;
    private String name;
    private double price;
    private int quantity;
    private double total;

    public Order(String orderId, String name, double price, int quantity, double total) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }
}

