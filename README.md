# 🍔 Cocina Adaptada: Patrón Adapter en Java

Ejemplo práctico del **patrón de diseño Adapter** para la exposición de Ingeniería de Software 2.

**Grupo 5:** Juanita Mejía · Santiago Pinzón · Nicolás Espinosa · Hernando García

---

## El problema

Un restaurante de hamburguesas tiene una **pantalla de cocina** que muestra los pedidos a los cocineros. Esa pantalla solo entiende pedidos con un formato propio: cliente, platos, total en pesos y notas.

El restaurante recibe pedidos de **Rappi**, **DiDi Food** e **iFood**, y cada plataforma los manda a su manera:

| Lo que necesita la cocina | Rappi | DiDi Food | iFood |
|---|---|---|---|
| `cliente()` | `customerName` | `usuario.nombre_completo` | `cliente.nome` |
| `items()` | lista `products` | **un solo texto**: `"2x Hamburguesa \| 1x Papas"` | lista `itens` |
| `totalPesos()` | `totalCents` (en **centavos**) | **texto** `"$ 38.500"` | **no lo manda** (hay que sumarlo) |
| `notas()` | `userComments` | `nota_cliente` | `observacoes` |

No podemos modificar el código de Rappi, DiDi ni iFood porque son de terceros. Tampoco queremos llenar la cocina de `if (es Rappi) ... else if (es DiDi) ...`.

**Solución:** un **Adapter** por plataforma. Cada uno traduce el pedido ajeno a la interfaz `Pedido` que la cocina ya entiende.

## Las piezas del patrón

| Rol del patrón | Clase en este proyecto | Qué hace |
|---|---|---|
| **Client** | `cocina.PantallaCocina` | Lógica de negocio: recibe y muestra pedidos. Solo conoce `Pedido`. |
| **Target** | `cocina.Pedido` (interfaz) | Lo que la cocina espera. |
| **Adaptee** | `RappiOrder`, `DidiPedido`, `IFoodOrder` | Los formatos de cada plataforma (simulan SDKs que no podemos tocar). |
| **Adapter** | `RappiAdapter`, `DidiAdapter`, `IFoodAdapter` | `implements Pedido` y **tiene adentro** el pedido de la plataforma. Traduce y delega. |

```
PantallaCocina ──usa──▶ «interface» Pedido ◁┈┈implementa┈┈ RappiAdapter ──tiene un──▶ RappiOrder
                                            ◁┈┈implementa┈┈ DidiAdapter  ──tiene un──▶ DidiPedido
                                            ◁┈┈implementa┈┈ IFoodAdapter ──tiene un──▶ IFoodOrder
```

Ejemplo, el adapter de Rappi:

```java
public class RappiAdapter implements Pedido {      // ES un Pedido
    private final RappiOrder rappi;                // TIENE un pedido de Rappi

    public String cliente()    { return rappi.getCustomerName(); }
    public double totalPesos() { return rappi.getTotalCents() / 100.0; }   // centavos -> pesos
    // ...
}
```

## Estructura del proyecto

```
src/
├── cocina/          ← Nuestro dominio (Client + Target). No importa nada externo.
│   ├── Pedido.java
│   ├── ItemPedido.java
│   └── PantallaCocina.java
├── plataformas/     ← "SDKs" de terceros simulados (Adaptees). No se modifican.
│   ├── rappi/
│   ├── didi/
│   └── ifood/
├── adaptadores/     ← Los Adapters: la frontera entre lo nuestro y lo ajeno.
│   ├── RappiAdapter.java
│   ├── DidiAdapter.java
│   └── IFoodAdapter.java
├── antes/           ← Cómo se vería SIN el patrón (código acoplado, solo para comparar).
│   └── PantallaCocinaAcoplada.java
└── app/             ← Servidor web y registro de plataformas.
    ├── Main.java
    └── Json.java
web/
└── index.html       ← Página interactiva de la demo
```

## Cómo ejecutarlo

**Requisito:** Java **JDK 17 o superior** (comprueba con `java -version`). No necesita Maven, Gradle ni librerías externas.

### Opción 1: con el script

- **Windows:** doble clic en `run.bat`
- **Mac / Linux:** `./run.sh` (si no tiene permisos: `chmod +x run.sh`)

### Opción 2: a mano

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```

### Opción 3: desde el IDE (IntelliJ / NetBeans / VS Code)

Abre la carpeta del proyecto, marca `src` como carpeta de código fuente y ejecuta `app.Main`.

Luego abre **http://localhost:14080** en el navegador y pide domicilios con los botones. Cada ticket también se imprime en la consola.

## 🎬 La prueba de fuego: conectar iFood en vivo

Al principio **iFood no está conectado**. Si le das clic, la cocina responde que no tiene adapter para leer ese pedido.

Para conectarlo:

1. Abre `src/app/Main.java`.
2. Quita el `//` de esta línea:
   ```java
   // registrar("ifood", IFoodAPI::proximoPedido, IFoodAdapter::new, IFoodOrder::toJson);
   ```
3. Detén el programa (`Ctrl + C`) y vuelve a ejecutarlo.

iFood ya funciona, y **`PantallaCocina` no se tocó ni una línea**. Eso es el principio **Abierto/Cerrado**: el sistema queda abierto a nuevas plataformas y cerrado a modificaciones.

## Principios que se ven en el código

- **Composición sobre herencia:** cada adapter *tiene* un pedido externo (`private final RappiOrder rappi`), no hereda de él.
- **Programar contra interfaces:** `PantallaCocina` solo usa `Pedido`, nunca un adapter concreto.
- **Responsabilidad única:** la traducción vive en los adapters; la cocina solo cocina.
- **Abierto/Cerrado:** agregar iFood = una clase nueva + una línea de registro.

## Referencias

- Freeman, E. & Robson, E. *Head First Design Patterns* (2.ª ed.), Cap. 7: Adapter y Facade.
- Refactoring.Guru: [Adapter](https://refactoring.guru/es/design-patterns/adapter)
- Gamma, Helm, Johnson & Vlissides. *Design Patterns* (GoF), 1994.

> Los datos de Rappi, DiDi Food e iFood son simulados con fines educativos. Este proyecto no se conecta a ninguna API real.
