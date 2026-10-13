<p align="center">
  <img src="docs/img/banner.png" alt="Cocina Adaptada: el patrón Adapter explicado con pedidos de Rappi, DiDi e iFood" width="100%">
</p>

<p align="center">
  <a href="https://github.com/Chocax/patron-adapter-grupo5/actions/workflows/build.yml"><img src="https://github.com/Chocax/patron-adapter-grupo5/actions/workflows/build.yml/badge.svg" alt="Compilar y probar"></a>
  <img src="https://img.shields.io/badge/Java-17%2B-e2601e?logo=openjdk&logoColor=white" alt="Java 17+">
  <img src="https://img.shields.io/badge/dependencias-0-17325c" alt="Sin dependencias">
  <img src="https://img.shields.io/badge/patr%C3%B3n-Adapter-ffc928?labelColor=1b1b1f" alt="Patrón Adapter">
  <img src="https://img.shields.io/badge/ISW--2-Grupo%205-17325c" alt="Grupo 5">
</p>

<p align="center">
  <b>Ejemplo práctico del patrón de diseño Adapter</b> para la exposición de Ingeniería de Software 2.<br>
  Juanita Mejía · Santiago Pinzón · Nicolás Espinosa · Hernando García
</p>

<p align="center">
  <img src="docs/img/demo.gif" alt="Demo: llegan pedidos de Rappi y DiDi, cada adapter los traduce y aparecen en la pantalla de cocina; iFood falla porque no tiene adapter" width="90%">
</p>

---

## 📑 Contenido

- [El problema](#-el-problema)
- [La solución: las piezas del patrón](#-la-solución-las-piezas-del-patrón)
- [Cómo ejecutarlo](#-cómo-ejecutarlo)
- [La prueba de fuego: conectar iFood en vivo](#-la-prueba-de-fuego-conectar-ifood-en-vivo)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Principios que se ven en el código](#-principios-que-se-ven-en-el-código)
- [Referencias](#-referencias)

## 🤔 El problema

Un restaurante de hamburguesas tiene una **pantalla de cocina** que muestra los pedidos a los cocineros. Esa pantalla solo entiende pedidos con un formato propio: cliente, platos, total en pesos y notas.

El restaurante recibe pedidos de **Rappi**, **DiDi Food** e **iFood**, y cada plataforma los manda a su manera:

| Lo que necesita la cocina | Rappi | DiDi Food | iFood |
|---|---|---|---|
| `cliente()` | `customerName` | `usuario.nombre_completo` | `cliente.nome` |
| `items()` | lista `products` | **un solo texto**: `"2x Hamburguesa \| 1x Papas"` | lista `itens` |
| `totalPesos()` | `totalCents` (en **centavos**) | **texto** `"$ 38.500"` | **no lo manda** (hay que sumarlo) |
| `notas()` | `userComments` | `nota_cliente` | `observacoes` |

No podemos modificar el código de Rappi, DiDi ni iFood porque son de terceros. Tampoco queremos llenar la cocina de `if (es Rappi) ... else if (es DiDi) ...`. En [`src/antes/PantallaCocinaAcoplada.java`](src/antes/PantallaCocinaAcoplada.java) está cómo se vería eso.

## 💡 La solución: las piezas del patrón

Un **Adapter** por plataforma. Cada uno traduce el pedido ajeno a la interfaz `Pedido` que la cocina ya entiende.

| Rol del patrón | Clase en este proyecto | Qué hace |
|---|---|---|
| **Client** | [`PantallaCocina`](src/cocina/PantallaCocina.java) | Lógica de negocio: recibe y muestra pedidos. Solo conoce `Pedido`. |
| **Target** | [`Pedido`](src/cocina/Pedido.java) (interfaz) | Lo que la cocina espera. |
| **Adaptee** | [`RappiOrder`](src/plataformas/rappi/RappiOrder.java), [`DidiPedido`](src/plataformas/didi/DidiPedido.java), [`IFoodOrder`](src/plataformas/ifood/IFoodOrder.java) | Los formatos de cada plataforma (simulan SDKs que no podemos tocar). |
| **Adapter** | [`RappiAdapter`](src/adaptadores/RappiAdapter.java), [`DidiAdapter`](src/adaptadores/DidiAdapter.java), [`IFoodAdapter`](src/adaptadores/IFoodAdapter.java) | `implements Pedido` y **tiene adentro** el pedido de la plataforma. Traduce y delega. |

```mermaid
classDiagram
    direction LR
    class PantallaCocina {
        +recibir(Pedido)
    }
    class Pedido {
        <<interface>>
        +cliente() String
        +items() List~ItemPedido~
        +totalPesos() double
        +notas() String
    }
    class RappiAdapter {
        -RappiOrder rappi
    }
    class DidiAdapter {
        -DidiPedido didi
    }
    class IFoodAdapter {
        -IFoodOrder ifood
    }
    class RappiOrder {
        +getCustomerName()
        +getTotalCents()
    }
    class DidiPedido {
        +getDetalle()
        +getValorTotal()
    }
    class IFoodOrder {
        +getCliente()
        +getItens()
    }
    PantallaCocina --> Pedido : usa
    Pedido <|.. RappiAdapter
    Pedido <|.. DidiAdapter
    Pedido <|.. IFoodAdapter
    RappiAdapter --> RappiOrder : tiene un
    DidiAdapter --> DidiPedido : tiene un
    IFoodAdapter --> IFoodOrder : tiene un
```

Así se ve el adapter de Rappi:

```java
public class RappiAdapter implements Pedido {      // ES un Pedido
    private final RappiOrder rappi;                // TIENE un pedido de Rappi

    public String cliente()    { return rappi.getCustomerName(); }
    public double totalPesos() { return rappi.getTotalCents() / 100.0; }   // centavos -> pesos
    // ...
}
```

Y así viaja un pedido por el sistema:

```mermaid
sequenceDiagram
    participant R as Rappi (externo)
    participant A as RappiAdapter
    participant C as PantallaCocina
    R->>A: RappiOrder (inglés, centavos)
    C->>A: cliente(), items(), totalPesos()
    A->>R: getCustomerName(), getProducts(), getTotalCents()
    A-->>C: datos en el formato de la cocina
    Note over C: La cocina nunca supo que era Rappi
```

## 🚀 Cómo ejecutarlo

**Requisito:** Java **JDK 17 o superior** (comprueba con `java -version`). No necesita Maven, Gradle ni librerías externas.

| Sistema | Comando |
|---|---|
| Windows | Doble clic en `run.bat` |
| Mac / Linux | `./run.sh` (si no tiene permisos: `chmod +x run.sh`) |
| A mano | `javac -encoding UTF-8 -d out $(find src -name "*.java")` y luego `java -cp out app.Main` |
| IDE | Abre la carpeta, marca `src` como código fuente y ejecuta `app.Main` |

Luego abre **http://localhost:14080** en el navegador y pide domicilios con los botones. Cada ticket también se imprime en la consola.

<p align="center">
  <img src="docs/img/demo.png" alt="Captura de la demo con pedidos en la pantalla de cocina" width="90%">
</p>

## 🎬 La prueba de fuego: conectar iFood en vivo

Al principio **iFood no está conectado**. Si le das clic, la cocina responde que no tiene adapter para leer ese pedido:

<p align="center">
  <img src="docs/img/demo-sin-adapter.png" alt="Error: la cocina no entiende pedidos de iFood porque no hay adapter" width="80%">
</p>

Para conectarlo:

1. Abre [`src/app/Main.java`](src/app/Main.java).
2. Quita el `//` de esta línea:
   ```java
   // registrar("ifood", IFoodAPI::proximoPedido, IFoodAdapter::new, IFoodOrder::toJson);
   ```
3. Detén el programa (`Ctrl + C`) y vuelve a ejecutarlo.

iFood ya funciona, y **`PantallaCocina` no se tocó ni una línea**. Eso es el principio **Abierto/Cerrado**: el sistema queda abierto a nuevas plataformas y cerrado a modificaciones.

## 📁 Estructura del proyecto

```
src/
├── cocina/          ← Nuestro dominio (Client + Target). No importa nada externo.
├── plataformas/     ← "SDKs" de terceros simulados (Adaptees). No se modifican.
│   ├── rappi/
│   ├── didi/
│   └── ifood/
├── adaptadores/     ← Los Adapters: la frontera entre lo nuestro y lo ajeno.
├── antes/           ← Cómo se vería SIN el patrón (código acoplado, solo para comparar).
└── app/             ← Servidor web y registro de plataformas.
web/
└── index.html       ← Página interactiva de la demo
.github/workflows/
└── build.yml        ← Compila y prueba los adapters en cada push
```

## 🧱 Principios que se ven en el código

- **Composición sobre herencia:** cada adapter *tiene* un pedido externo (`private final RappiOrder rappi`), no hereda de él.
- **Programar contra interfaces:** `PantallaCocina` solo usa `Pedido`, nunca un adapter concreto.
- **Responsabilidad única:** la traducción vive en los adapters; la cocina solo cocina.
- **Abierto/Cerrado:** agregar iFood = una clase nueva + una línea de registro.

## 📚 Referencias

- Freeman, E. & Robson, E. *Head First Design Patterns* (2.ª ed.), Cap. 7: Adapter y Facade.
- Refactoring.Guru: [Adapter](https://refactoring.guru/es/design-patterns/adapter)
- Gamma, Helm, Johnson & Vlissides. *Design Patterns* (GoF), 1994.

> [!NOTE]
> Los datos de Rappi, DiDi Food e iFood son simulados con fines educativos. Este proyecto no se conecta a ninguna API real.
