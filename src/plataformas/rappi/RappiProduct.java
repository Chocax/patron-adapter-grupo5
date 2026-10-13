package plataformas.rappi;

/** Parte del "SDK de Rappi". Código de terceros: NO lo podemos modificar. */
public class RappiProduct {

    private final String name;
    private final int quantity;
    private final long unitPriceCents;

    public RappiProduct(String name, int quantity, long unitPriceCents) {
        this.name = name;
        this.quantity = quantity;
        this.unitPriceCents = unitPriceCents;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getUnitPriceCents() {
        return unitPriceCents;
    }
}
