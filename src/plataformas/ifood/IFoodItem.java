package plataformas.ifood;

/** Parte del "SDK de iFood". NO lo podemos modificar. */
public class IFoodItem {

    private final String descricao;
    private final int quantidade;
    private final double precoUnitario;

    public IFoodItem(String descricao, int quantidade, double precoUnitario) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }
}
