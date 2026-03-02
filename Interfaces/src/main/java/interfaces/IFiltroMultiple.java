package interfaces;

/**
 * Contrato adicional para filtros que procesan múltiples archivos
 * de forma concurrente usando hilos de ejecución.
 *
 * Aplica a: ID3, ID4, ID7, ID8 (filtros con IN: File[...] o OUT: File[...]).
 *
 * Principio SOLID:
 *   - Interface Segregation: extiende IFiltro solo para filtros que
 *     realmente necesitan procesamiento concurrente.
 */
public interface IFiltroMultiple extends IFiltro {

    /**
     * Número de hilos a usar en el procesamiento concurrente.
     * Cada hilo procesa un elemento de la lista de forma independiente.
     */
    int getNumHilos();
}
