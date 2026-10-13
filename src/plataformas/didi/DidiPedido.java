package plataformas.didi;

import plataformas.DatosDePrueba;

/**
 * ADAPTEE #2 — Así manda DiDi Food los pedidos.
 *
 * Simula una librería de terceros: NO la podemos modificar.
 * Diferencias con lo que espera la cocina:
 *   - El nombre del cliente viene metido dentro de otro objeto (usuario.nombreCompleto).
 *   - Los platos vienen en UN SOLO TEXTO: "2x Hamburguesa doble | 1x Papas grandes".
 *   - El total viene como TEXTO con formato: "$ 38.500".
 */
public class DidiPedido {

    private final String idOrden;
    private final UsuarioDidi usuario;
    private final String detalle;
    private final String valorTotal;
    private final String notaCliente;

    public DidiPedido(String idOrden, UsuarioDidi usuario, String detalle, String valorTotal, String notaCliente) {
        this.idOrden = idOrden;
        this.usuario = usuario;
        this.detalle = detalle;
        this.valorTotal = valorTotal;
        this.notaCliente = notaCliente;
    }

    public String getIdOrden() {
        return idOrden;
    }

    public UsuarioDidi getUsuario() {
        return usuario;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public String getNotaCliente() {
        return notaCliente;
    }

    /** El payload tal cual lo mandaría la API de DiDi (para mostrarlo en la demo). */
    public String toJson() {
        return "{\"id_orden\":" + DatosDePrueba.json(idOrden)
                + ",\"usuario\":{\"nombre_completo\":" + DatosDePrueba.json(usuario.getNombreCompleto()) + "}"
                + ",\"detalle\":" + DatosDePrueba.json(detalle)
                + ",\"valor_total\":" + DatosDePrueba.json(valorTotal)
                + ",\"nota_cliente\":" + DatosDePrueba.json(notaCliente) + "}";
    }
}
