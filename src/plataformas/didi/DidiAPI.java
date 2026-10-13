package plataformas.didi;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import plataformas.DatosDePrueba;

/** Simula el webhook de DiDi Food: cada llamada devuelve un pedido nuevo en formato DiDi. */
public final class DidiAPI {

    private DidiAPI() {
    }

    public static DidiPedido siguientePedido() {
        List<DatosDePrueba.Linea> lineas = DatosDePrueba.lineas();
        String detalle = lineas.stream()
                .map(l -> l.cantidad() + "x " + l.plato().nombre())
                .collect(Collectors.joining(" | "));
        long total = lineas.stream()
                .mapToLong(l -> l.cantidad() * l.plato().precioPesos())
                .sum();
        String valorTotal = "$ " + String.format(Locale.US, "%,d", total).replace(',', '.');
        return new DidiPedido("DD-" + DatosDePrueba.numero(4), new UsuarioDidi(DatosDePrueba.cliente()),
                detalle, valorTotal, DatosDePrueba.nota());
    }
}
