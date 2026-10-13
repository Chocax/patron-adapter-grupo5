package adaptadores;

import java.util.List;

import cocina.ItemPedido;
import cocina.Pedido;
import plataformas.rappi.RappiOrder;

/**
 * ADAPTER — Hace que un pedido de Rappi "parezca" un Pedido de nuestra cocina.
 *
 *   implements Pedido      -> ES un Pedido (cumple el Target)
 *   private RappiOrder     -> TIENE un pedido de Rappi adentro (composición)
 *
 * Cada método traduce y delega. No hay lógica de negocio aquí, solo traducción.
 */
public class RappiAdapter implements Pedido {

    private final RappiOrder rappi;

    public RappiAdapter(RappiOrder rappi) {
        this.rappi = rappi;
    }

    @Override
    public String codigo() {
        return rappi.getOrderId();
    }

    @Override
    public String plataforma() {
        return "Rappi";
    }

    @Override
    public String cliente() {
        return rappi.getCustomerName();              // customerName -> cliente
    }

    @Override
    public List<ItemPedido> items() {
        return rappi.getProducts().stream()          // products -> items
                .map(p -> new ItemPedido(p.getName(), p.getQuantity()))
                .toList();
    }

    @Override
    public double totalPesos() {
        return rappi.getTotalCents() / 100.0;        // centavos -> pesos
    }

    @Override
    public String notas() {
        return rappi.getUserComments();              // userComments -> notas
    }
}
