package com.workorderit.concurrency.unsafe;

import com.workorderit.concurrency.model.Order;

/**
 * VERSION INSEGURA - Solo para fines educativos.
 *
 * Race condition: varios hilos leen y escriben totalOrders/totalAmount
 * al mismo tiempo sin coordinacion. Resultado: datos corruptos.
 *
 *   Hilo A lee totalOrders = 5
 *   Hilo B lee totalOrders = 5   <- B no sabe que A ya lo leyo
 *   Hilo A escribe totalOrders = 6
 *   Hilo B escribe totalOrders = 6  <- incremento de A se PIERDE
 */
public class UnsafeOrderProcessor {

    // Variables compartidas sin proteccion - NO thread-safe
    private int    totalOrders = 0;
    private double totalAmount = 0.0;

    public void process(Order order) {
        simulateWork();
        totalOrders++;                     // read -> increment -> write: NO atomico
        totalAmount += order.getAmount();  // idem: tres pasos no atomicos
    }

    public int getTotalOrders()    { return totalOrders; }
    public double getTotalAmount() { return totalAmount; }

    public void reset() {
        totalOrders = 0;
        totalAmount = 0.0;
    }

    private void simulateWork() {
        try { Thread.sleep(0, 100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
