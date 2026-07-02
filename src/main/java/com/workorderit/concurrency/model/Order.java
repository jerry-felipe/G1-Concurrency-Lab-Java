package com.workorderit.concurrency.model;

public class Order {

    private final String id;
    private final double amount;

    public Order(String id, double amount) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID invalido.");
        if (amount <= 0) throw new IllegalArgumentException("Monto invalido.");
        this.id = id;
        this.amount = amount;
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("Order{id='%s', amount=%.2f}", id, amount);
    }
}
