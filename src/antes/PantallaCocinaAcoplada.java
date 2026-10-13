package antes;

import plataformas.didi.DidiPedido;
import plataformas.rappi.RappiOrder;
import plataformas.rappi.RappiProduct;

/**
 * ASÍ SE VERÍA SIN EL PATRÓN ADAPTER. (Esta clase NO se usa en la app;
 * está aquí para mostrar el problema en la exposición.)
 *
 * Problemas:
 * 1. La cocina importa clases de Rappi y DiDi: está ACOPLADA a ellas.
 * 2. Cada plataforma nueva obliga a abrir y modificar esta clase (rompe
 * Abierto/Cerrado).
 * 3. La lógica de cocina y la de traducción están mezcladas (rompe
 * Responsabilidad Única).
 * 4. Si Rappi cambia su SDK, se rompe la cocina.
 */
public class PantallaCocinaAcoplada {

    public void recibir(Object pedidoExterno) {
        if (pedidoExterno instanceof RappiOrder rappi) { // IF DE RAPPI
            System.out.println("Cliente: " + rappi.getCustomerName());
            for (RappiProduct p : rappi.getProducts()) {
                System.out.println(p.getQuantity() + "x " + p.getName());
            }
            System.out.println("Total: " + rappi.getTotalCents() / 100.0);

        } else if (pedidoExterno instanceof DidiPedido didi) { // IF DE DiDi
            System.out.println("Cliente: " + didi.getUsuario().getNombreCompleto());
            for (String linea : didi.getDetalle().split("\\|")) {
                System.out.println(linea.trim());
            }
            System.out.println("Total: " + didi.getValorTotal().replaceAll("[^0-9]", ""));

        }
        // ¿Y iFood? -> Hay que volver a abrir ESTA clase y agregar otro else-if.
        // ¿Y Uber Eats? -> Otro else-if más... Así crece sin control.
    }
}
