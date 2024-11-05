package com.example;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Order {
    private final String product;
    private final int quantity;
    private final double price;

    public Order(String product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

public class EjemploEstudiante {
    public static void main(String[] args) {
        List<Order> orders = Arrays.asList(
            new Order("Product A", 2, 50.0),
            new Order("Product B", 1, 30.0),
            new Order("Product A", 1, 50.0),
            new Order("Product C", 3, 20.0),
            new Order("Product A", 4, 50.0),
            new Order("Product B", 2, 30.0),
            new Order("Product C", 1, 20.0)
        );

        // Process orders for Product A
        processOrders(orders, "Product A")
            .subscribe(total -> System.out.println("Total sales for Product A: " + total));

        // Process orders for Product B
        processOrders(orders, "Product B")
            .subscribe(total -> System.out.println("Total sales for Product B: " + total));

        // Process orders for Product C
        processOrders(orders, "Product C")
            .subscribe(total -> System.out.println("Total sales for Product C: " + total));
    }

    public static Observable<Double> processOrders(List<Order> orders, String product) {
        return Observable.fromIterable(orders)
            .filter(order -> product.equals(order.getProduct()))
            .map(order -> order.getQuantity() * order.getPrice())
            .reduce(0.0, Double::sum)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS);
    }

    public static Observable<Long> countOrders(List<Order> orders, String product) {
        return Observable.fromIterable(orders)
            .filter(order -> product.equals(order.getProduct()))
            .count()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .defaultIfEmpty(0L); 
    }
    

    public static Observable<String> processOrderDetails(List<Order> orders, String product) {
        return Observable.fromIterable(orders)
            .filter(order -> product.equals(order.getProduct()))
            .map(order -> "Product: " + order.getProduct() +
                          ", Quantity: " + order.getQuantity() +
                          ", Price: " + order.getPrice())
            .toList() // Convertimos a una lista
            .flatMapObservable(list -> {
                if (list.isEmpty()) {
                    return Observable.just("No orders found for " + product); // Emitimos un mensaje si no hay órdenes
                } else {
                    return Observable.fromIterable(list); // Emitimos la lista si hay órdenes
                }
            })
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS);
    }
    
    
    
}
