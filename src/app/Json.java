package app;

import java.util.stream.Collectors;

import cocina.Pedido;

/** Convierte a JSON sin librerías externas (solo lo necesario para la demo). */
final class Json {

    private Json() {
    }

    static String texto(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                default -> sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    /** Fíjense: aquí solo se usa la interfaz Pedido. No importa de qué plataforma vino. */
    static String pedido(Pedido p) {
        String items = p.items().stream()
                .map(i -> "{\"nombre\":" + texto(i.nombre()) + ",\"cantidad\":" + i.cantidad() + "}")
                .collect(Collectors.joining(",", "[", "]"));
        return "{\"codigo\":" + texto(p.codigo())
                + ",\"plataforma\":" + texto(p.plataforma())
                + ",\"cliente\":" + texto(p.cliente())
                + ",\"items\":" + items
                + ",\"totalPesos\":" + (long) p.totalPesos()
                + ",\"notas\":" + texto(p.notas()) + "}";
    }
}
