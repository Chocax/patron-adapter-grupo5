package adaptadores;

import java.util.Arrays;
import java.util.List;

import cocina.ItemPedido;
import cocina.Pedido;
import plataformas.didi.DidiPedido;

/**
 * ADAPTER — Hace que un pedido de DiDi Food "parezca" un Pedido de nuestra cocina.
 *
 * DiDi manda los platos y el total como TEXTO, así que este adapter
 * tiene que "desarmar" ese texto. Esa complejidad queda encerrada aquí:
 * la cocina nunca se entera.
 */
public class DidiAdapter implements Pedido {

    private final DidiPedido didi;

    public DidiAdapter(DidiPedido didi) {
        this.didi = didi;
    }

    @Override
    public String codigo() {
        return didi.getIdOrden();
    }

    @Override
    public String plataforma() {
        return "DiDi Food";
    }

    @Override
    public String cliente() {
        return didi.getUsuario().getNombreCompleto();   // usuario.nombreCompleto -> cliente
    }

    @Override
    public List<ItemPedido> items() {
        // "2x Hamburguesa doble | 1x Papas grandes"  ->  [ (Hamburguesa doble, 2), (Papas grandes, 1) ]
        return Arrays.stream(didi.getDetalle().split("\\|"))
                .map(String::trim)
                .map(linea -> {
                    int x = linea.indexOf("x ");
                    int cantidad = Integer.parseInt(linea.substring(0, x));
                    String nombre = linea.substring(x + 2);
                    return new ItemPedido(nombre, cantidad);
                })
                .toList();
    }

    @Override
    public double totalPesos() {
        // "$ 38.500"  ->  38500.0
        String soloDigitos = didi.getValorTotal().replaceAll("[^0-9]", "");
        return Double.parseDouble(soloDigitos);
    }

    @Override
    public String notas() {
        return didi.getNotaCliente();
    }
}
