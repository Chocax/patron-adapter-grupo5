package plataformas.ifood;

/** Parte del "SDK de iFood" (empresa brasileña, todo en portugués). NO lo podemos modificar. */
public class IFoodCliente {

    private final String nome;
    private final String telefone;

    public IFoodCliente(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }
}
