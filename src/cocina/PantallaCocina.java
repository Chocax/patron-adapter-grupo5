package cocina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * CLIENT (la lógica de negocio).
 *
 * Recibe pedidos y los muestra a los cocineros. Fíjense en los imports:
 * esta clase no importa NADA de Rappi, DiDi ni iFood. Solo conoce {@link Pedido}.
 * Por eso, cuando llega una plataforma nueva, esta clase no se toca.
 */
public class PantallaCocina {

    private final List<Pedido> enCola = new ArrayList<>();

    public synchronized void recibir(Pedido pedido) {
        enCola.add(pedido);
        imprimirTicket(pedido);
    }

    public synchronized List<Pedido> pedidos() {
        return Collections.unmodifiableList(new ArrayList<>(enCola));
    }

    public synchronized void limpiar() {
        enCola.clear();
    }

    private void imprimirTicket(Pedido p) {
        System.out.println();
        System.out.println("+------------------------------------------+");
        System.out.printf("| NUEVO PEDIDO %-12s  (%s)%n", p.codigo(), p.plataforma());
        System.out.printf("| Cliente: %s%n", p.cliente());
        for (ItemPedido item : p.items()) {
            System.out.printf("|   %dx %s%n", item.cantidad(), item.nombre());
        }
        if (!p.notas().isBlank()) {
            System.out.printf("| Nota: %s%n", p.notas());
        }
        String total = String.format(Locale.US, "%,.0f", p.totalPesos()).replace(',', '.');
        System.out.println("| Total: $ " + total);
        System.out.println("+------------------------------------------+");
    }
}
