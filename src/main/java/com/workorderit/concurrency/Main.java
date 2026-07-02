package com.workorderit.concurrency;

import com.workorderit.concurrency.model.Order;
import com.workorderit.concurrency.safe.SafeOrderProcessor;
import com.workorderit.concurrency.unsafe.UnsafeOrderProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Punto de entrada del laboratorio de concurrencia.
 *
 * Ejecuta el mismo conjunto de ordenes con dos procesadores:
 *   1. UnsafeOrderProcessor  -> demuestra la race condition
 *   2. SafeOrderProcessor    -> demuestra la solucion atomica
 */
public class Main {

    private static final int    NUM_ORDERS    = 1_000;
    private static final int    NUM_THREADS   = 20;
    private static final double ORDER_AMOUNT  = 150.75;

    public static void main(String[] args) throws InterruptedException {
        List<Order> orders = generateOrders();
        double expectedTotal  = NUM_ORDERS * ORDER_AMOUNT;
        int    expectedOrders = NUM_ORDERS;

        printHeader(expectedOrders, expectedTotal);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  VERSION INSEGURA (sin sincronizacion)");
        System.out.println("=".repeat(60));
        UnsafeOrderProcessor unsafe = new UnsafeOrderProcessor();
        long unsafeTime = runConcurrently(orders, unsafe::process);
        printResults(unsafe.getTotalOrders(), unsafe.getTotalAmount(), expectedOrders, expectedTotal, unsafeTime);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  VERSION SEGURA (AtomicInteger + AtomicLong)");
        System.out.println("=".repeat(60));
        SafeOrderProcessor safe = new SafeOrderProcessor();
        long safeTime = runConcurrently(orders, safe::process);
        printResults(safe.getTotalOrders(), safe.getTotalAmount(), expectedOrders, expectedTotal, safeTime);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  CONCLUSION");
        System.out.println("=".repeat(60));
        System.out.printf("  Ordenes perdidas (unsafe)      : %d%n", expectedOrders - unsafe.getTotalOrders());
        System.out.printf("  Diferencia de monto (unsafe)   : $%.2f%n", expectedTotal - unsafe.getTotalAmount());
        System.out.println("\n  La version segura conserva el paralelismo sin comprometer los datos.");
        System.out.println("=".repeat(60));
    }

    private static long runConcurrently(List<Order> orders, java.util.function.Consumer<Order> processor)
            throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        long start = System.currentTimeMillis();
        for (Order order : orders) executor.submit(() -> processor.accept(order));
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        return System.currentTimeMillis() - start;
    }

    private static void printResults(int actual, double amount, int expOrders, double expAmount, long ms) {
        boolean ok1 = actual == expOrders;
        boolean ok2 = Math.abs(amount - expAmount) < 0.01;
        System.out.printf("  Ordenes procesadas : %,d / %,d  %s%n", actual, expOrders, ok1 ? "OK" : "ERROR");
        System.out.printf("  Monto total        : $%,.2f / $%,.2f  %s%n", amount, expAmount, ok2 ? "OK" : "ERROR");
        System.out.printf("  Tiempo             : %d ms%n", ms);
        System.out.printf("  Estado             : %s%n", (ok1 && ok2) ? "DATOS CORRECTOS" : "DATOS CORRUPTOS");
    }

    private static void printHeader(int n, double total) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  CONCURRENCY LAB - Work Order IT");
        System.out.println("=".repeat(60));
        System.out.printf("  Ordenes    : %,d%n", n);
        System.out.printf("  Hilos      : %d%n", NUM_THREADS);
        System.out.printf("  Monto/orden: $%.2f%n", ORDER_AMOUNT);
        System.out.printf("  Total esp. : $%,.2f%n", total);
    }

    private static List<Order> generateOrders() {
        List<Order> list = new ArrayList<>(NUM_ORDERS);
        for (int i = 1; i <= NUM_ORDERS; i++)
            list.add(new Order("ORD-" + String.format("%04d", i), ORDER_AMOUNT));
        return list;
    }
}
