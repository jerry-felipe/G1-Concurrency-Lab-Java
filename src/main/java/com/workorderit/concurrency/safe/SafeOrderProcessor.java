package com.workorderit.concurrency.safe;

import com.workorderit.concurrency.model.Order;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * VERSION SEGURA - Usando variables atomicas de java.util.concurrent.atomic
 *
 * AtomicInteger y AtomicLong garantizan operaciones CAS (Compare-And-Swap)
 * a nivel de hardware: aunque 20 hilos llamen a process() al mismo tiempo,
 * cada incremento se aplica correctamente sin perder actualizaciones.
 *
 * Ventajas sobre synchronized:
 *   Sin bloqueos (lock-free) -> mejor rendimiento bajo alta concurrencia
 *   Sin riesgo de deadlock
 *
 * El monto se almacena en centavos (long) para usar AtomicLong,
 * ya que no existe AtomicDouble en Java estandar.
 */
public class SafeOrderProcessor {

    private final AtomicInteger totalOrders      = new AtomicInteger(0);
    private final AtomicLong    totalAmountCents = new AtomicLong(0L);

    public void process(Order order) {
        simulateWork();
        totalOrders.incrementAndGet();
        totalAmountCents.addAndGet(Math.round(order.getAmount() * 100));
    }

    public int getTotalOrders()    { return totalOrders.get(); }
    public double getTotalAmount() { return totalAmountCents.get() / 100.0; }

    public void reset() {
        totalOrders.set(0);
        totalAmountCents.set(0L);
    }

    private void simulateWork() {
        try { Thread.sleep(0, 100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
