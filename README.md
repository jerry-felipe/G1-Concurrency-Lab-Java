# ¿Por qué la concurrencia puede hacer tu sistema más rápido… o destruirlo en producción?

**Concurrencia en Java: más velocidad o más caos, depende de cómo la diseñes**

Este repositorio presenta un caso práctico sobre concurrencia en Java, enfocado en demostrar cómo la ejecución concurrente puede mejorar el rendimiento de un sistema, pero también puede provocar errores graves cuando el estado compartido no se controla correctamente.

Dominar la concurrencia ya no es opcional para un desarrollador moderno. Los sistemas actuales procesan múltiples usuarios, transacciones, eventos y servicios al mismo tiempo. Cuando la concurrencia se diseña bien, permite mayor velocidad, escalabilidad y mejor uso de recursos. Cuando se ignora o se implementa mal, puede generar errores difíciles de detectar, datos inconsistentes, bloqueos, lentitud y fallos críticos en producción.

El verdadero reto no es ejecutar varias tareas al mismo tiempo, sino hacerlo con control, sincronización y seguridad.

---

## Estructura del proyecto

```
src/main/java/com/workorderit/concurrency/
├── model/
│   └── Order.java                    # Modelo inmutable de una orden
├── unsafe/
│   └── UnsafeOrderProcessor.java     # Versión sin sincronización (race condition)
├── safe/
│   └── SafeOrderProcessor.java       # Versión segura con AtomicInteger + AtomicLong
└── Main.java                         # Ejecuta ambas versiones y compara resultados
pom.xml                               # Configuración Maven (Java 21 LTS)
```

---

## Objetivo del repositorio

Explicar, mediante un ejemplo práctico en Java, cómo un sistema puede procesar órdenes en paralelo para mejorar su rendimiento, pero terminar generando resultados incorrectos si varios hilos modifican el mismo estado compartido sin coordinación.

El repositorio también muestra cómo corregir este problema utilizando mecanismos seguros de concurrencia disponibles en Java, conservando el beneficio del procesamiento paralelo sin comprometer la exactitud de los datos.

---

## Tecnología principal

- **Lenguaje:** Java 21 LTS
- **Enfoque:** Concurrencia, estado compartido, atomicidad y seguridad entre hilos
- **Build:** Maven
- **Tipo de proyecto:** Ejemplo técnico y educativo para desarrolladores

---

## El problema que se analiza

El sistema simula el procesamiento paralelo de 1.000 órdenes usando 20 hilos concurrentes. Cada orden actualiza dos valores compartidos:

- Cantidad de órdenes procesadas
- Monto total acumulado

### Versión insegura — `UnsafeOrderProcessor`

Usa variables primitivas (`int`, `double`) sin ningún mecanismo de protección. Cuando varios hilos ejecutan `process()` al mismo tiempo, ocurre una **condición de carrera**:

```
Hilo A lee totalOrders = 5
Hilo B lee totalOrders = 5   ← B no sabe que A ya lo leyó
Hilo A escribe totalOrders = 6
Hilo B escribe totalOrders = 6  ← el incremento de A se PIERDE
```

El sistema parece funcionar, pero los datos finales están dañados.

### Versión segura — `SafeOrderProcessor`

Usa `AtomicInteger` y `AtomicLong` de `java.util.concurrent.atomic`. Estas clases garantizan operaciones **CAS (Compare-And-Swap)** a nivel de hardware: cada incremento es atómico, sin bloqueos y sin riesgo de deadlock.

---

## Cómo ejecutarlo

```bash
git clone https://github.com/jerry-felipe/G1-Concurrency-Lab-Java.git
cd G1-Concurrency-Lab-Java
mvn package
java -jar target/concurrency-lab.jar
```

### Salida esperada

```
============================================================
  CONCURRENCY LAB - Work Order IT
============================================================
  Ordenes    : 1.000
  Hilos      : 20
  Total esp. : $150.750,00

============================================================
  VERSION INSEGURA (sin sincronizacion)
============================================================
  Ordenes procesadas : 987 / 1.000  ERROR   ← pierde órdenes
  Monto total        : $148.490,25 / $150.750,00  ERROR
  Estado             : DATOS CORRUPTOS

============================================================
  VERSION SEGURA (AtomicInteger + AtomicLong)
============================================================
  Ordenes procesadas : 1.000 / 1.000  OK
  Monto total        : $150.750,00 / $150.750,00  OK
  Estado             : DATOS CORRECTOS
```

---

## Conceptos clave

- **Condición de carrera** — múltiples hilos acceden al mismo estado sin coordinación
- **Atomicidad** — una operación se completa sin interrupciones externas
- **CAS (Compare-And-Swap)** — mecanismo hardware para actualizaciones seguras sin locks
- **Lock-free programming** — concurrencia sin `synchronized`, más eficiente bajo alta carga

---

## Dónde aparecen estos problemas en producción

APIs con alto volumen de solicitudes, sistemas transaccionales, bases de datos, colas de mensajes, microservicios, procesos batch, cachés compartidas y sistemas distribuidos.

---

## Idea clave

> La concurrencia bien dominada acelera el sistema.
> La concurrencia mal diseñada puede destruir la confiabilidad de una aplicación en producción.

---

## Autor

**Work Order IT**
Soluciones tecnológicas, arquitectura de software y formación técnica para equipos de desarrollo.

Este proyecto forma parte de una iniciativa educativa orientada a explicar, de forma práctica, cómo la concurrencia en Java puede mejorar el rendimiento de un sistema o comprometer su confiabilidad si no se diseña correctamente.

🌐 [www.workorder-it.net](https://www.workorder-it.net)
