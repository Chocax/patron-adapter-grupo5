package cocina;

import java.util.List;

/**
 * TARGET (la interfaz que el cliente espera).
 *
 * La pantalla de cocina SOLO sabe trabajar con objetos que cumplan esta interfaz.
 * No sabe que existen Rappi, DiDi ni iFood.
 */
public interface Pedido {

    String codigo();

    String plataforma();

    String cliente();

    List<ItemPedido> items();

    double totalPesos();

    String notas();
}
