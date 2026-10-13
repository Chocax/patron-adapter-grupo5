package app;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import adaptadores.DidiAdapter;
import adaptadores.IFoodAdapter;
import adaptadores.RappiAdapter;
import cocina.Pedido;
import cocina.PantallaCocina;
import plataformas.didi.DidiAPI;
import plataformas.didi.DidiPedido;
import plataformas.ifood.IFoodAPI;
import plataformas.ifood.IFoodOrder;
import plataformas.rappi.RappiAPI;
import plataformas.rappi.RappiOrder;

/**
 * Arranca la app: un servidor web muy simple (incluido en el JDK, sin
 * librerías)
 * que sirve la página de la demo y conecta las plataformas con la cocina.
 */
public class Main {

    private static final PantallaCocina cocina = new PantallaCocina();
    private static final Map<String, Integracion<?>> plataformas = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {

        // ===================================================================
        // Cada plataforma entra a la cocina A TRAVÉS DE SU ADAPTER.
        // registrar(nombre, API externa, Adapter, cómo se ve su JSON)
        // ===================================================================
        registrar("rappi", RappiAPI::siguientePedido, RappiAdapter::new, RappiOrder::toJson);
        registrar("didi", DidiAPI::siguientePedido, DidiAdapter::new, DidiPedido::toJson);

        // demo ifood
        // registrar("ifood", IFoodAPI::proximoPedido, IFoodAdapter::new,
        // IFoodOrder::toJson);
        //

        int puerto = Integer.parseInt(System.getenv().getOrDefault("PORT", "14080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);
        server.createContext("/", Main::paginaWeb);
        server.createContext("/api/plataformas", Main::listarPlataformas);
        server.createContext("/api/pedidos/", Main::llegaPedido);
        server.createContext("/api/cocina", Main::verCocina);
        server.start();

        System.out.println("==============================================");
        System.out.println("  Demo Patron Adapter - Cocina de domicilios");
        System.out.println("  Abre en el navegador:  http://localhost:" + puerto);
        System.out.println("  Plataformas conectadas: " + plataformas.keySet());
        System.out.println("==============================================");
    }

    // ---------------------------------------------------------------------

    /** Une la API externa con su adapter. Lo que sale de aquí ya es un Pedido. */
    record Integracion<T>(Supplier<T> api, Function<T, Pedido> adapter, Function<T, String> jsonOriginal) {
        Llegada recibir() {
            T pedidoExterno = api.get();
            return new Llegada(jsonOriginal.apply(pedidoExterno), adapter.apply(pedidoExterno));
        }
    }

    record Llegada(String jsonOriginal, Pedido pedido) {
    }

    private static <T> void registrar(String nombre, Supplier<T> api,
            Function<T, Pedido> adapter, Function<T, String> json) {
        plataformas.put(nombre, new Integracion<>(api, adapter, json));
    }

    // Rutas

    private static void llegaPedido(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) {
            responder(ex, 405, "{\"error\":\"Usa POST\"}");
            return;
        }
        String nombre = ex.getRequestURI().getPath().substring("/api/pedidos/".length());
        Integracion<?> integracion = plataformas.get(nombre);
        if (integracion == null) {
            responder(ex, 422, "{\"error\":" + Json.texto(
                    "La cocina no entiende pedidos de '" + nombre + "': todavía no hay un adapter registrado.") + "}");
            return;
        }

        Llegada llegada = integracion.recibir();
        cocina.recibir(llegada.pedido()); // la cocina solo recibe un Pedido

        responder(ex, 200, "{\"original\":" + llegada.jsonOriginal()
                + ",\"adapter\":" + Json.texto(llegada.pedido().getClass().getSimpleName())
                + ",\"pedido\":" + Json.pedido(llegada.pedido()) + "}");
    }

    private static void verCocina(HttpExchange ex) throws IOException {
        if ("DELETE".equals(ex.getRequestMethod())) {
            cocina.limpiar();
        }
        List<Pedido> pedidos = cocina.pedidos();
        responder(ex, 200, pedidos.stream().map(Json::pedido).collect(Collectors.joining(",", "[", "]")));
    }

    private static void listarPlataformas(HttpExchange ex) throws IOException {
        responder(ex, 200, plataformas.keySet().stream().map(Json::texto)
                .collect(Collectors.joining(",", "[", "]")));
    }

    private static void paginaWeb(HttpExchange ex) throws IOException {
        Path pagina = buscarPagina();
        if (!"/".equals(ex.getRequestURI().getPath()) || pagina == null) {
            ex.sendResponseHeaders(404, -1);
            ex.close();
            return;
        }
        byte[] html = Files.readAllBytes(pagina);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(200, html.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(html);
        }
    }

    private static Path buscarPagina() {
        for (String ruta : List.of("web/index.html", "../web/index.html", "adapter-pedidos/web/index.html")) {
            Path p = Path.of(ruta);
            if (Files.exists(p)) {
                return p;
            }
        }
        return null;
    }

    private static void responder(HttpExchange ex, int codigo, String json) throws IOException {
        byte[] cuerpo = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(codigo, cuerpo.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(cuerpo);
        }
    }
}
