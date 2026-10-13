package plataformas.didi;

/** Parte del "SDK de DiDi Food". Código de terceros: NO lo podemos modificar. */
public class UsuarioDidi {

    private final String nombreCompleto;

    public UsuarioDidi(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
}
