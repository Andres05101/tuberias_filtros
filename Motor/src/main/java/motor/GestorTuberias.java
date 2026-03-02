package motor;

import datos.TipoDato;
import interfaces.IFiltro;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestor central de tuberías.
 *
 * Responsabilidades:
 *   - Mantener el registro de filtros disponibles (los 8 filtros).
 *   - Crear y almacenar tuberías con nombre.
 *   - Proveer información sobre filtros para la consola.
 *
 * Principios SOLID:
 *   - Single Responsibility: solo gestiona tuberías y el catálogo de filtros.
 *   - Dependency Inversion: trabaja con IFiltro, no con clases concretas.
 */
public class GestorTuberias {

    private final List<IFiltro>            filtrosDisponibles;
    private final Map<String, Tuberia>     tuberias = new LinkedHashMap<>();

    public GestorTuberias(List<IFiltro> filtrosDisponibles) {
        this.filtrosDisponibles = new ArrayList<>(filtrosDisponibles);
    }

    // ── Gestión de tuberías ───────────────────────────────────────

    /** Crea y registra una nueva tubería con el nombre dado. */
    public Tuberia crearTuberia(String nombre) {
        Tuberia t = new Tuberia(nombre);
        tuberias.put(nombre, t);
        return t;
    }

    /** Elimina una tubería registrada por nombre. */
    public boolean eliminarTuberia(String nombre) {
        return tuberias.remove(nombre) != null;
    }

    /** Devuelve todas las tuberías registradas. */
    public Map<String, Tuberia> getTuberias() {
        return new LinkedHashMap<>(tuberias);
    }

    /** Devuelve una tubería por nombre, o null si no existe. */
    public Tuberia getTuberia(String nombre) {
        return tuberias.get(nombre);
    }

    // ── Gestión de filtros ────────────────────────────────────────

    /** Devuelve la lista completa de filtros disponibles. */
    public List<IFiltro> getFiltrosDisponibles() {
        return new ArrayList<>(filtrosDisponibles);
    }

    /** Busca un filtro por su ID. Devuelve null si no existe. */
    public IFiltro getFiltroById(int id) {
        return filtrosDisponibles.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica si un filtro es compatible con el último filtro de una Tuberia real.
     */
    public boolean esCompatible(Tuberia tuberia, IFiltro filtroNuevo) {
        if (tuberia.estaVacia()) return true;
        List<IFiltro> filtros = tuberia.getFiltros();
        TipoDato ultimaSalida = filtros.get(filtros.size() - 1).getTipoSalida();
        return Tuberia.sonCompatibles(ultimaSalida, filtroNuevo.getTipoEntrada());
    }

    /**
     * Verifica si un filtro es compatible con el último filtro de una lista de filtros.
     * Usado por la VistaEscritorio al construir el pipeline antes de guardarlo.
     */
    public boolean esCompatible(List<IFiltro> filtrosActuales, IFiltro filtroNuevo) {
        if (filtrosActuales.isEmpty()) return true;
        TipoDato ultimaSalida = filtrosActuales.get(filtrosActuales.size() - 1).getTipoSalida();
        return Tuberia.sonCompatibles(ultimaSalida, filtroNuevo.getTipoEntrada());
    }

    // ── Información ───────────────────────────────────────────────

    /** Devuelve un resumen tabular de los filtros disponibles. */
    public String resumenFiltros() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  %-4s %-35s %-15s %-15s%n",
                "ID", "Nombre", "IN", "OUT"));
        sb.append("  " + "─".repeat(72) + "\n");
        for (IFiltro f : filtrosDisponibles) {
            sb.append(String.format("  %-4d %-35s %-15s %-15s%n",
                    f.getId(), f.getNombre(),
                    f.getTipoEntrada(), f.getTipoSalida()));
        }
        return sb.toString();
    }
}
