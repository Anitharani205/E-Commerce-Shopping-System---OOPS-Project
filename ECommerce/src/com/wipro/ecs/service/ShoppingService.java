package com.wipro.ecs.service;

import java.util.ArrayList;

import com.wipro.ecs.entity.*;
import com.wipro.ecs.util.*;

public class ShoppingService {

    private ArrayList<User> users;
    private ArrayList<Product> products;
    private ArrayList<Order> orders;
    private int orderCount = 1;
    public ShoppingService(ArrayList<User> users,
                           ArrayList<Product> products,
                           ArrayList<Order> orders) {
        this.users = users;
        this.products = products;
        this.orders = orders;
    }

    public boolean validateUser(String userId)
            throws InvalidUserException {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return true;
            }
        }
        throw new InvalidUserException();
    }

    public Product findProduct(String productId)
            throws ProductNotFoundException {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        throw new ProductNotFoundException();
    }

    public void checkStock(String productId, int quantity)
            throws OutOfStockException, ProductNotFoundException {
        Product product = findProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException();
        }
    }

    public Order placeOrder(String userId, ArrayList<CartItem> cart)
            throws InvalidUserException, ProductNotFoundException,
                   OutOfStockException, OrderOperationException {

        validateUser(userId);

        if (cart == null || cart.isEmpty()) {
            throw new OrderOperationException();
        }

        double total = calculateTotal(cart);

        for (CartItem item : cart) {
            checkStock(item.getProductId(), item.getQuantity());
            Product product = findProduct(item.getProductId());
            product.setStock(product.getStock() - item.getQuantity());
        }

        String orderId = "ORD" + orderCount++;

        Order order = new Order(orderId, userId, cart, total);
        orders.add(order);

        return order;
    }

    private double calculateTotal(ArrayList<CartItem> cart)
            throws ProductNotFoundException {

        double total = 0;
        for (CartItem item : cart) {
            Product product = findProduct(item.getProductId());
            total += product.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void cancelOrder(String orderId)
            throws OrderNotFoundException {

        Order orderToCancel = null;

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                orderToCancel = order;
                break;
            }
        }

        if (orderToCancel == null) {
            throw new OrderNotFoundException();
        }

        for (CartItem item : orderToCancel.getItems()) {
            try {
                Product product = findProduct(item.getProductId());
                product.setStock(product.getStock() + item.getQuantity());
            } catch (ProductNotFoundException e) {
                System.out.println(e);
            }
        }

        orders.remove(orderToCancel);
    }

    public void printUserOrders(String userId) {
        for (Order order : orders) {
            if (order.getUserId().equals(userId)) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Total Amount: ₹" + order.getTotalAmount());
                System.out.println("Items:");
                for (CartItem item : order.getItems()) {
                    System.out.println("Product ID: " + item.getProductId() + ", Quantity: " + item.getQuantity());
                }
            }
        }
    }
}
