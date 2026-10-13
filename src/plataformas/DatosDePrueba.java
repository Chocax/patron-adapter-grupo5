package plataformas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Menú y clientes de mentiras para que los simuladores de cada plataforma generen pedidos. */
public final class DatosDePrueba {

    public record Plato(String nombre, long precioPesos) {
    }

    public record Linea(Plato plato, int cantidad) {
    }

    private static final Random RANDOM = new Random();

    private static final List<Plato> MENU = List.of(
            new Plato("Hamburguesa clásica", 18_900),
            new Plato("Hamburguesa doble", 25_900),
            new Plato("Perro caliente", 14_900),
            new Plato("Alitas BBQ x6", 21_900),
            new Plato("Papas grandes", 8_900),
            new Plato("Gaseosa", 4_500),
            new Plato("Malteada de vainilla", 11_900));

    private static final List<String> CLIENTES = List.of(
            "Laura Gómez", "Andrés Rodríguez", "Valentina Torres", "Camilo Herrera",
            "Mariana López", "Juan David Ruiz", "Sofía Castro", "Felipe Moreno");

    private static final List<String> NOTAS = List.of(
            "Sin cebolla, por favor", "Salsas aparte", "Extra queso",
            "Llamar al llegar, el timbre no sirve", "", "");

    private DatosDePrueba() {
    }

    public static String cliente() {
        return CLIENTES.get(RANDOM.nextInt(CLIENTES.size()));
    }

    public static String nota() {
        return NOTAS.get(RANDOM.nextInt(NOTAS.size()));
    }

    public static String telefono() {
        return "3" + (100_000_000 + RANDOM.nextInt(899_999_999));
    }

    public static int numero(int digitos) {
        int min = (int) Math.pow(10, digitos - 1);
        return min + RANDOM.nextInt(min * 9);
    }

    /** Entre 1 y 3 platos distintos, con cantidades de 1 a 3. */
    public static List<Linea> lineas() {
        List<Plato> menu = new ArrayList<>(MENU);
        Collections.shuffle(menu, RANDOM);
        int cuantos = 1 + RANDOM.nextInt(3);
        List<Linea> lineas = new ArrayList<>();
        for (int i = 0; i < cuantos; i++) {
            lineas.add(new Linea(menu.get(i), 1 + RANDOM.nextInt(3)));
        }
        return lineas;
    }

    public static String json(String texto) {
        return "\"" + texto.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
