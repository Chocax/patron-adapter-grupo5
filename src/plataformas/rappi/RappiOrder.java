package plataformas.rappi;

import java.util.List;
import java.util.stream.Collectors;

import plataformas.DatosDePrueba;

/**
 * ADAPTEE #1 — Así manda Rappi los pedidos.
 *
 * Simula una librería de terceros: NO la podemos modificar.
 * Diferencias con lo que espera la cocina:
 *   - Todo está en inglés (customerName, products, userComments).
 *   - Los precios vienen en CENTAVOS (long), no en pesos.
 */
public class RappiOrder {

    private final String orderId;
    private final String customerName;
    private final List<RappiProduct> products;
    private final long totalCents;
    private final String userComments;

    public RappiOrder(String orderId, String customerName, List<RappiProduct> products,
                      long totalCents, String userComments) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.products = products;
        this.totalCents = totalCents;
        this.userComments = userComments;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<RappiProduct> getProducts() {
        return products;
    }

    public long getTotalCents() {
        return totalCents;
    }

    public String getUserComments() {
        return userComments;
    }

    /** El payload tal cual lo mandaría la API de Rappi (para mostrarlo en la demo). */
    public String toJson() {
        String items = products.stream()
                .map(p -> "{\"name\":" + DatosDePrueba.json(p.getName())
                        + ",\"quantity\":" + p.getQuantity()
                        + ",\"unitPriceCents\":" + p.getUnitPriceCents() + "}")
                .collect(Collectors.joining(","));
        return "{\"orderId\":" + DatosDePrueba.json(orderId)
                + ",\"customerName\":" + DatosDePrueba.json(customerName)
                + ",\"products\":[" + items + "]"
                + ",\"totalCents\":" + totalCents
                + ",\"userComments\":" + DatosDePrueba.json(userComments) + "}";
    }
}
