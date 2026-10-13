package plataformas.rappi;

import java.util.List;

import plataformas.DatosDePrueba;

/** Simula el webhook de Rappi: cada llamada devuelve un pedido nuevo en formato Rappi. */
public final class RappiAPI {

    private RappiAPI() {
    }

    public static RappiOrder siguientePedido() {
        List<RappiProduct> products = DatosDePrueba.lineas().stream()
                .map(l -> new RappiProduct(l.plato().nombre(), l.cantidad(), l.plato().precioPesos() * 100))
                .toList();
        long totalCents = products.stream()
                .mapToLong(p -> p.getQuantity() * p.getUnitPriceCents())
                .sum();
        return new RappiOrder("RP-" + DatosDePrueba.numero(5), DatosDePrueba.cliente(),
                products, totalCents, DatosDePrueba.nota());
    }
}
