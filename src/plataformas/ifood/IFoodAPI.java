package plataformas.ifood;

import java.util.List;

import plataformas.DatosDePrueba;

/** Simula el webhook de iFood: cada llamada devuelve um pedido novo no formato iFood. */
public final class IFoodAPI {

    private IFoodAPI() {
    }

    public static IFoodOrder proximoPedido() {
        List<IFoodItem> itens = DatosDePrueba.lineas().stream()
                .map(l -> new IFoodItem(l.plato().nombre(), l.cantidad(), l.plato().precioPesos()))
                .toList();
        return new IFoodOrder("IF-" + DatosDePrueba.numero(6),
                new IFoodCliente(DatosDePrueba.cliente(), DatosDePrueba.telefono()),
                itens, DatosDePrueba.nota());
    }
}
