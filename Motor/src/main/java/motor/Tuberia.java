package motor;

import datos.PaqueteDatos;
import datos.TipoDato;
import interfaces.IFiltro;

import java.util.ArrayList;
import java.util.List;

/**
 * Tubería: cadena ordenada de filtros que se ejecutan secuencialmente.
 *
 * Cada filtro recibe como entrada la salida del filtro anterior.
 * Aplica adaptación automática de tipos entre filtros (wrapping/unwrapping).
 *
 * Patrón: Pipe-and-Filter — el Motor es la tubería, los IFiltro son los filtros.
 *
 * Reglas de adaptación automática (compatibilidad entre tipos):
 *   TEXTO     ↔ LISTA_TEXTO    (wrap/unwrap)
 *   BINARIO   ↔ LISTA_BINARIO  (wrap/unwrap)
 *   BASE64    ↔ LISTA_BASE64   (wrap/unwrap)
 *   IMAGEN    ↔ LISTA_IMAGEN   (wrap/unwrap)
 */
public class Tuberia {

    private final String        nombre;
    private final List<IFiltro> filtros = new ArrayList<>();

    public Tuberia(String nombre) {
        this.nombre = nombre;
    }

    // ── Construcción de la tubería ────────────────────────────────

    /**
     * Agrega un filtro al final de la tubería.
     * Valida la compatibilidad de tipos con el filtro anterior.
     *
     * @return true si se agregó correctamente, false si hay incompatibilidad.
     */
    public boolean agregarFiltro(IFiltro filtro) {
        if (!filtros.isEmpty()) {
            TipoDato salida  = filtros.get(filtros.size() - 1).getTipoSalida();
            TipoDato entrada = filtro.getTipoEntrada();

            if (!sonCompatibles(salida, entrada)) {
                System.err.println("  ✘ Incompatible: " + salida + " → " + entrada
                        + " (Filtro " + filtros.get(filtros.size()-1).getId()
                        + " → Filtro " + filtro.getId() + ")");
                return false;
            }
        }
        filtros.add(filtro);
        return true;
    }

    /**
     * Verifica si dos tipos de datos son compatibles directa o con auto-conversión.
     * Compatible si son iguales, o si uno es la versión lista/simple del otro.
     */
    public static boolean sonCompatibles(TipoDato salida, TipoDato entrada) {
        if (salida == entrada) return true;
        // Simple → Lista (wrap automático)
        if (salida == TipoDato.TEXTO   && entrada == TipoDato.LISTA_TEXTO)   return true;
        if (salida == TipoDato.BINARIO && entrada == TipoDato.LISTA_BINARIO) return true;
        if (salida == TipoDato.BASE64  && entrada == TipoDato.LISTA_BASE64)  return true;
        if (salida == TipoDato.IMAGEN  && entrada == TipoDato.LISTA_IMAGEN)  return true;
        // Lista → Simple (unwrap automático: toma el primer elemento)
        if (salida == TipoDato.LISTA_TEXTO   && entrada == TipoDato.TEXTO)   return true;
        if (salida == TipoDato.LISTA_BINARIO && entrada == TipoDato.BINARIO) return true;
        if (salida == TipoDato.LISTA_BASE64  && entrada == TipoDato.BASE64)  return true;
        if (salida == TipoDato.LISTA_IMAGEN  && entrada == TipoDato.IMAGEN)  return true;
        return false;
    }

    // ── Ejecución ─────────────────────────────────────────────────

    /**
     * Ejecuta todos los filtros de la tubería en secuencia.
     * Adapta automáticamente los tipos entre filtros.
     *
     * @param entradaInicial Paquete de datos inicial (proporcionado por el usuario/consola).
     * @return Resultado final tras pasar por todos los filtros.
     */
    public PaqueteDatos ejecutar(PaqueteDatos entradaInicial) {
        System.out.println("\n╔══ Ejecutando tubería: " + nombre + " ══╗");
        System.out.println("  Filtros: " + describir());

        PaqueteDatos actual = entradaInicial;

        for (int i = 0; i < filtros.size(); i++) {
            IFiltro filtro = filtros.get(i);
            System.out.println("\n  [Paso " + (i + 1) + "/" + filtros.size() + "] "
                    + filtro.getNombre());

            // Adaptar el paquete si es necesario antes de procesar
            actual = adaptar(actual, filtro.getTipoEntrada());

            // Ejecutar el filtro
            actual = filtro.procesar(actual);
        }

        System.out.println("\n╚══ Tubería completada. Resultado: " + actual + " ══╝");
        return actual;
    }

    /**
     * Adapta un paquete de datos al tipo esperado por el siguiente filtro.
     * Aplica wrap (simple→lista) o unwrap (lista→primer elemento) según necesidad.
     */
    private PaqueteDatos adaptar(PaqueteDatos paquete, TipoDato tipoEsperado) {
        TipoDato actual = paquete.getTipo();
        if (actual == tipoEsperado) return paquete;

        // Simple → Lista
        if (!actual.esLista() && tipoEsperado.esLista()
                && actual.aLista() == tipoEsperado) {
            System.out.println("  ↪ Auto-wrap: " + actual + " → " + tipoEsperado);
            return paquete.aLista();
        }

        // Lista → Simple (primer elemento)
        if (actual.esLista() && !tipoEsperado.esLista()
                && actual.aSingle() == tipoEsperado) {
            System.out.println("  ↪ Auto-unwrap: " + actual + " → " + tipoEsperado + " (1er elemento)");
            return paquete.primerElemento();
        }

        return paquete; // sin adaptación posible, el filtro lo manejará
    }

    // ── Consultas ─────────────────────────────────────────────────

    public String        getNombre()  { return nombre; }
    public List<IFiltro> getFiltros() { return new ArrayList<>(filtros); }
    public int           size()       { return filtros.size(); }
    public boolean       estaVacia()  { return filtros.isEmpty(); }

    /** Devuelve el tipo de entrada requerido por el primer filtro. */
    public TipoDato getTipoEntrada() {
        return filtros.isEmpty() ? null : filtros.get(0).getTipoEntrada();
    }

    /** Devuelve el tipo de salida del último filtro. */
    public TipoDato getTipoSalida() {
        return filtros.isEmpty() ? null : filtros.get(filtros.size() - 1).getTipoSalida();
    }

    /** Descripción textual de la cadena de filtros (e.g. "1 → 2 → 6"). */
    public String describir() {
        if (filtros.isEmpty()) return "(vacía)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filtros.size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(filtros.get(i).getId())
              .append(" (").append(filtros.get(i).getNombre()).append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Tuberia[" + nombre + "]: " + describir();
    }
}
