package plataformas.ifood;

import java.util.List;
import java.util.stream.Collectors;

import plataformas.DatosDePrueba;

/**
 * ADAPTEE #3 — Así manda iFood los pedidos.
 *
 * Simula una librería de terceros: NO la podemos modificar.
 * Diferencias con lo que espera la cocina:
 *   - Todo está en portugués (cliente.nome, itens, observacoes).
 *   - NO manda el total: solo el precio unitario de cada ítem.
 */
public class IFoodOrder {

    private final String codigo;
    private final IFoodCliente cliente;
    private final List<IFoodItem> itens;
    private final String observacoes;

    public IFoodOrder(String codigo, IFoodCliente cliente, List<IFoodItem> itens, String observacoes) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.itens = itens;
        this.observacoes = observacoes;
    }

    public String getCodigo() {
        return codigo;
    }

    public IFoodCliente getCliente() {
        return cliente;
    }

    public List<IFoodItem> getItens() {
        return itens;
    }

    public String getObservacoes() {
        return observacoes;
    }

    /** El payload tal cual lo mandaría la API de iFood (para mostrarlo en la demo). */
    public String toJson() {
        String items = itens.stream()
                .map(i -> "{\"descricao\":" + DatosDePrueba.json(i.getDescricao())
                        + ",\"quantidade\":" + i.getQuantidade()
                        + ",\"precoUnitario\":" + (long) i.getPrecoUnitario() + "}")
                .collect(Collectors.joining(","));
        return "{\"codigo\":" + DatosDePrueba.json(codigo)
                + ",\"cliente\":{\"nome\":" + DatosDePrueba.json(cliente.getNome())
                + ",\"telefone\":" + DatosDePrueba.json(cliente.getTelefone()) + "}"
                + ",\"itens\":[" + items + "]"
                + ",\"observacoes\":" + DatosDePrueba.json(observacoes) + "}";
    }
}
