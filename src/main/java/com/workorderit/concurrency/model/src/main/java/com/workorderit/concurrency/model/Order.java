package com.workorderit.concurrency.model;

/**
 * Representa una orden de compra en el sistema.
 * Inmutable por diseno: una vez creada, sus valores no cambian.
 */
public class Order {

    private final String id;
    private final double amount;

    public Order(String id, double amount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de la orden no puede estar vacio.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("Order{id='%s', amount=%.2f}", id, amount);
    }
}
