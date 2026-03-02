# Tuberías y Filtros

Aplicación de escritorio en Java que implementa la arquitectura **Pipes & Filters** (Tuberías y Filtros). Cada filtro es un componente independiente (JAR) que procesa datos y pasa el resultado al siguiente filtro de la cadena.

---

## Arquitectura

```
┌──────────────┐     ┌────────────────┐     ┌──────────┐     ┌──────────────┐     ┌────────┐
│  Presentacion│────▶│  FabricaFiltros│────▶│  Motor   │────▶│   Filtro N   │────▶│  Datos │
│ (VistaEscrit)│     │  (Singleton)   │     │(Tuberia) │     │(1..8 JAR sep)│     │(DTO)   │
└──────────────┘     └────────────────┘     └──────────┘     └──────────────┘     └────────┘
```

### Módulos Maven

| Módulo | Rol | Artefacto |
|--------|-----|-----------|
| `Datos` | DTO transversal (`PaqueteDatos`, `TipoDato`) | `Datos` |
| `Interfaces` | Contratos (`IFiltro`, `IFiltroMultiple`) | `Interfaces` |
| `1_CargarArchivo` | Filtro ID1 | `1-CargarArchivo` |
| `2_TextoABinario` | Filtro ID2 | `2-TextoABinario` |
| `3_FiltroImagenes` | Filtro ID3 | `3-FiltroImagenes` |
| `4_BinarioABase64` | Filtro ID4 | `4-BinarioABase64` |
| `5_Base64ABinario` | Filtro ID5 | `5-Base64ABinario` |
| `6_EncriptarSHA256` | Filtro ID6 | `6-EncriptarSHA256` |
| `7_BuscarPalabra` | Filtro ID7 | `7-BuscarPalabra` |
| `8_ContarOcurrencias` | Filtro ID8 | `8-ContarOcurrencias` |
| `Motor` | Pipeline (`Tuberia`, `GestorTuberias`) | `Motor` |
| `FabricaFiltros` | Singleton + Factory Method | `FabricaFiltros` |
| `Presentacion` | GUI Swing (`VistaEscritorio`) | `Presentacion` |
| `Inicio` | Punto de entrada (`main`) | `TuberiasFiltros-ejecutable` |

---

## Filtros disponibles

| ID | Nombre | Entrada | Salida | Descripción |
|----|--------|---------|--------|-------------|
| **ID1** | CargarArchivo | PATH | TEXTO | Lee un archivo de texto y entrega su contenido |
| **ID2** | TextoABinario | TEXTO | BINARIO | Convierte cada byte a su representación de 8 bits (`01001000 01101001`) |
| **ID3** | FiltroImagenes | IMAGEN | LISTA_IMAGEN | Aplica 4 filtros en paralelo: escala de grises, reducción 50%, brillo y rotación 90° |
| **ID4** | BinarioABase64 | LISTA_BINARIO | LISTA_BASE64 | Codifica datos binarios en Base64 con hilos concurrentes |
| **ID5** | Base64ABinario | BASE64 | BINARIO | Decodifica Base64 a los bytes originales |
| **ID6** | EncriptarSHA256 | TEXTO | TEXTO | Genera el hash SHA-256 del contenido (hexadecimal) |
| **ID7** | BuscarPalabra | TEXTO | TEXTO | Indica si una palabra existe en el texto (SÍ/NO + líneas) |
| **ID8** | ContarOcurrencias | TEXTO | TEXTO | Cuenta cuántas veces aparece una palabra específica |

### Conexiones válidas entre filtros

```
ID1 ──▶ ID2 ──▶ ID4 ──▶ ID5
                          └──▶ ID6 ──▶ ID7
                                    └──▶ ID8
ID1 ──▶ ID6 ──▶ ID7
             └──▶ ID8
ID3  (imagen → 4 PNGs independientes)
```

---

## Patrones de diseño aplicados

| Patrón | Dónde | Descripción |
|--------|-------|-------------|
| **Singleton** | `FabricaFiltros` | Una única instancia de la fábrica en toda la aplicación |
| **Factory Method** | `FabricaFiltros` | `crearFiltro(int id)`, `crearGestorTuberias()` |
| **Pipes & Filters** | `Tuberia` + `GestorTuberias` | Encadenamiento y validación automática de tipos |
| **DTO** | `PaqueteDatos` | Transporta datos entre filtros sin exponer implementación |
| **Dependency Injection** | `InicioAplicacion` → `VistaEscritorio` | Inyección por constructor |

---

## Requisitos

- **Java 11** o superior
- **Maven 3.6** o superior

---

## Compilar y ejecutar

Desde la carpeta raíz `Tuberias_Filtros/`:

```bash
# Compilar todo el proyecto
mvn clean package -q

# Ejecutar la aplicación de escritorio
java -jar Inicio/target/TuberiasFiltros-ejecutable.jar
```

O en un solo comando:

```bash
mvn clean package -q && java -jar Inicio/target/TuberiasFiltros-ejecutable.jar
```

---

## Archivos de salida

Los resultados se guardan automáticamente en la carpeta `output/` (se crea al iniciar si no existe).

| Filtro | Nombre del archivo | Contenido |
|--------|--------------------|-----------|
| ID1 | `id1_20260301_153022.txt` | Texto leído del archivo |
| ID2 | `id2_20260301_153045.txt` | Cadena de bits (`01001000 01101001`) |
| ID3 | `id3_gris_....png` / `id3_reduccion50pct_....png` / `id3_brillo_....png` / `id3_rotacion90_....png` | 4 imágenes PNG |
| ID4 | `id4_lista_20260301_153130.b64` | Texto en Base64 |
| ID5 | `id5_20260301_153150.txt` | Bytes decodificados |
| ID6 | `id6_20260301_153200.txt` | Hash SHA-256 hexadecimal |
| ID7 | `id7_20260301_153220.txt` | Reporte SÍ/NO con líneas |
| ID8 | `id8_20260301_153240.txt` | Número de ocurrencias |

> El timestamp (`YYYYMMDD_HHmmss`) evita sobreescribir archivos de ejecuciones anteriores.
> La carpeta `output/` está en `.gitignore` y no se sube al repositorio.

---

## Estructura del proyecto

```
Tuberias_Filtros/
├── pom.xml                        ← POM padre (multi-módulo)
├── .gitignore
├── README.md
│
├── Datos/                         ← DTO transversal
│   └── src/main/java/datos/
│       ├── PaqueteDatos.java
│       └── TipoDato.java
│
├── Interfaces/                    ← Contratos de filtros
│   └── src/main/java/interfaces/
│       ├── IFiltro.java
│       └── IFiltroMultiple.java
│
├── 1_CargarArchivo/               ← Filtro ID1
├── 2_TextoABinario/               ← Filtro ID2
├── 3_FiltroImagenes/              ← Filtro ID3 (con hilos)
├── 4_BinarioABase64/              ← Filtro ID4 (con hilos)
├── 5_Base64ABinario/              ← Filtro ID5
├── 6_EncriptarSHA256/             ← Filtro ID6
├── 7_BuscarPalabra/               ← Filtro ID7
├── 8_ContarOcurrencias/           ← Filtro ID8
│
├── Motor/                         ← Lógica de pipeline
│   └── src/main/java/motor/
│       ├── Tuberia.java
│       └── GestorTuberias.java
│
├── FabricaFiltros/                ← Singleton + Factory (transversal)
│   └── src/main/java/fabrica/
│       └── FabricaFiltros.java
│
├── Presentacion/                  ← GUI Swing
│   └── src/main/java/presentacion/
│       └── VistaEscritorio.java
│
├── Inicio/                        ← Punto de entrada (main)
│   └── src/main/java/inicio/
│       └── InicioAplicacion.java
│
└── output/                        ← Resultados (generado, en .gitignore)
```
