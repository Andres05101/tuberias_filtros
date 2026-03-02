package fabrica;

import filtros.*;
import interfaces.IFiltro;
import motor.GestorTuberias;

import java.util.Arrays;
import java.util.List;

/**
 * Fábrica Centralizada de Objetos — Tuberías y Filtros.
 *
 * Componente TRANSVERSAL: visible y accesible desde todas las capas
 * (igual que FabricaDTO en ComponentesConCapas).
 *
 * Patrones aplicados:
 *   - Singleton: una única instancia de la fábrica en toda la aplicación.
 *   - Factory Method: un método de creación por cada tipo de objeto.
 *
 * Responsabilidades:
 *   - Crear instancias de los 8 filtros (IDs 1–8).
 *   - Crear el GestorTuberias con el catálogo completo de filtros.
 *
 * Principios SOLID:
 *   - Single Responsibility: centraliza TODA la creación de objetos.
 *   - Dependency Inversion: InicioAplicacion y Presentacion dependen de
 *     esta fábrica, no de las clases concretas de los filtros.
 *   - Open/Closed: agregar un nuevo filtro solo requiere añadir un
 *     método factory aquí, sin modificar el resto del sistema.
 */
public class FabricaFiltros {

    // ===================== SINGLETON =====================
    private static FabricaFiltros instancia;

    /** Constructor privado — impide instanciación directa. */
    private FabricaFiltros() { }

    /**
     * Devuelve la única instancia de FabricaFiltros.
     * Patrón Singleton — lazy initialization.
     */
    public static FabricaFiltros getInstance() {
        if (instancia == null) {
            instancia = new FabricaFiltros();
        }
        return instancia;
    }

    // ═══════════════════════════════════════════════════════════════
    // ══════════ FACTORY METHODS — Filtros individuales ═══════════
    // ═══════════════════════════════════════════════════════════════

    /**
     * ID1 — Carga el contenido de un archivo de texto.
     * IN: PATH → OUT: TEXTO
     */
    public IFiltro crearFiltro1() {
        return new CargarArchivoDeTexto();
    }

    /**
     * ID2 — Convierte texto a representación binaria (cadena de bits).
     * IN: TEXTO → OUT: BINARIO
     */
    public IFiltro crearFiltro2() {
        return new ConvertirTextoABinario();
    }

    /**
     * ID3 — Aplica 4 filtros de imagen en paralelo (4 hilos).
     * Genera: grises, reducción 50%, brillo, rotación 90°.
     * IN: IMAGEN → OUT: LISTA_IMAGEN
     */
    public IFiltro crearFiltro3() {
        return new FiltroImagenes();
    }

    /**
     * ID4 — Codifica datos binarios a Base64 con hilos concurrentes.
     * IN: LISTA_BINARIO → OUT: LISTA_BASE64
     */
    public IFiltro crearFiltro4() {
        return new ConvertirBinarioABase64();
    }

    /**
     * ID5 — Decodifica Base64 a bytes binarios originales.
     * IN: BASE64 → OUT: BINARIO
     */
    public IFiltro crearFiltro5() {
        return new ConvertirBase64ABinario();
    }

    /**
     * ID6 — Genera el hash SHA-256 del contenido de texto.
     * IN: TEXTO → OUT: TEXTO (hexadecimal de 64 chars)
     */
    public IFiltro crearFiltro6() {
        return new EncriptarArchivoTextoSHA256();
    }

    /**
     * ID7 — Verifica si una palabra existe en el texto (SÍ / NO).
     * IN: TEXTO → OUT: TEXTO
     *
     * @param palabraBuscar Palabra a buscar (case-insensitive).
     */
    public IFiltro crearFiltro7(String palabraBuscar) {
        return new BuscarPalabraEnArchivoTexto(
                (palabraBuscar != null && !palabraBuscar.trim().isEmpty())
                        ? palabraBuscar.trim()
                        : "la");
    }

    /**
     * ID8 — Cuenta cuántas veces aparece una palabra en el texto.
     * IN: TEXTO → OUT: TEXTO (número exacto de ocurrencias)
     *
     * @param palabraBuscar Palabra cuyas ocurrencias se cuentan (case-insensitive).
     */
    public IFiltro crearFiltro8(String palabraBuscar) {
        return new ListarPalabrasFrecuenciaDeOcurrencia(
                (palabraBuscar != null && !palabraBuscar.trim().isEmpty())
                        ? palabraBuscar.trim()
                        : "la");
    }

    // ═══════════════════════════════════════════════════════════════
    // ══════════ FACTORY METHOD — Catálogo por ID ═════════════════
    // ═══════════════════════════════════════════════════════════════

    /**
     * Crea un filtro por su ID numérico (1–8).
     * Para ID7 e ID8 usa la palabra por defecto "la".
     * Si necesitas palabra personalizada, usa crearFiltro7(palabra) o crearFiltro8(palabra).
     *
     * @param id Identificador del filtro (1–8).
     * @return Instancia nueva del filtro.
     * @throws IllegalArgumentException si el ID no existe.
     */
    public IFiltro crearFiltro(int id) {
        switch (id) {
            case 1: return crearFiltro1();
            case 2: return crearFiltro2();
            case 3: return crearFiltro3();
            case 4: return crearFiltro4();
            case 5: return crearFiltro5();
            case 6: return crearFiltro6();
            case 7: return crearFiltro7("la");
            case 8: return crearFiltro8("la");
            default: throw new IllegalArgumentException(
                    "No existe el filtro con ID=" + id + ". IDs válidos: 1–8.");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // ══════════ FACTORY METHOD — GestorTuberias ══════════════════
    // ═══════════════════════════════════════════════════════════════

    /**
     * Crea el GestorTuberias con el catálogo completo de los 8 filtros.
     * El ID7 se registra con la palabra "la" como placeholder;
     * la VistaEscritorio crea instancias nuevas al agregar a una tubería.
     *
     * @return Nueva instancia de GestorTuberias lista para usar.
     */
    public GestorTuberias crearGestorTuberias() {
        List<IFiltro> catalogo = Arrays.asList(
                crearFiltro1(),
                crearFiltro2(),
                crearFiltro3(),
                crearFiltro4(),
                crearFiltro5(),
                crearFiltro6(),
                crearFiltro7("(configurar)"),   // placeholder — la UI pide la palabra al usuario
                crearFiltro8("(configurar)")
        );
        return new GestorTuberias(catalogo);
    }
}
