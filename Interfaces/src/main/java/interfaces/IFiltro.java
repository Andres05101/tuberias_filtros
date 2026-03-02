package interfaces;

import datos.PaqueteDatos;
import datos.TipoDato;

/**
 * Contrato base que deben implementar todos los filtros.
 *
 * Principio SOLID:
 *   - Interface Segregation: contrato mínimo y cohesivo.
 *   - Dependency Inversion: Motor y Presentacion dependen de esta
 *     abstracción, no de implementaciones concretas.
 *
 * Flujo:
 *   PaqueteDatos(tipoEntrada) → [procesar()] → PaqueteDatos(tipoSalida)
 */
public interface IFiltro {

    /**
     * Procesa el paquete de entrada y devuelve el resultado.
     * Es la operación principal de cada filtro.
     */
    PaqueteDatos procesar(PaqueteDatos entrada);

    /** Identificador único del filtro (1–8). */
    int getId();

    /** Nombre descriptivo del filtro. */
    String getNombre();

    /** Descripción de lo que hace el filtro. */
    String getDescripcion();

    /** Tipo de dato que espera como entrada. */
    TipoDato getTipoEntrada();

    /** Tipo de dato que produce como salida. */
    TipoDato getTipoSalida();
}
