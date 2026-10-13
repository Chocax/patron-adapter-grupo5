package adaptadores;

import java.util.List;

import cocina.ItemPedido;
import cocina.Pedido;
import plataformas.ifood.IFoodOrder;

/**
 * ADAPTER — Hace que un pedido de iFood "parezca" un Pedido de nuestra cocina.
 *
 * Para conectar iFood NO tocamos PantallaCocina ni los otros adapters:
 * solo escribimos esta clase y la registramos en Main (principio Abierto/Cerrado).
 */
public class IFoodAdapter implements Pedido {

    private final IFoodOrder ifood;

    public IFoodAdapter(IFoodOrder ifood) {
        this.ifood = ifood;
    }

    @Override
    public String codigo() {
        return ifood.getCodigo();
    }

    @Override
    public String plataforma() {
        return "iFood";
    }

    @Override
    public String cliente() {
        return ifood.getCliente().getNome();          // cliente.nome -> cliente
    }

    @Override
    public List<ItemPedido> items() {
        return ifood.getItens().stream()              // itens -> items
                .map(i -> new ItemPedido(i.getDescricao(), i.getQuantidade()))
                .toList();
    }

    @Override
    public double totalPesos() {
        // iFood no manda el total: el adapter lo calcula
        return ifood.getItens().stream()
                .mapToDouble(i -> i.getQuantidade() * i.getPrecoUnitario())
                .sum();
    }

    @Override
    public String notas() {
        return ifood.getObservacoes();                // observacoes -> notas
    }
}
