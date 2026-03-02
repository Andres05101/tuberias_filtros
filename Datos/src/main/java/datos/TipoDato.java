package datos;

/**
 * Enumeración que define los tipos de datos que fluyen entre filtros.
 *
 * Tipos simples (un solo elemento):
 *   PATH         → Ruta de archivo (String como bytes)
 *   TEXTO        → Contenido textual (String como bytes UTF-8)
 *   BINARIO      → Representación binaria (cadena de bits como bytes)
 *   BASE64       → Contenido codificado en Base64
 *   IMAGEN       → Bytes de una imagen (formato PNG)
 *
 * Tipos múltiples (lista de elementos, procesados con hilos):
 *   LISTA_TEXTO      → Lista de contenidos de texto
 *   LISTA_BINARIO    → Lista de representaciones binarias
 *   LISTA_BASE64     → Lista de contenidos Base64
 *   LISTA_IMAGEN     → Lista de imágenes (salida de FiltroImagenes)
 */
public enum TipoDato {

    // ── Tipos simples ─────────────────────────────────────────────
    PATH,
    TEXTO,
    BINARIO,
    BASE64,
    IMAGEN,

    // ── Tipos múltiples (requieren hilos) ─────────────────────────
    LISTA_TEXTO,
    LISTA_BINARIO,
    LISTA_BASE64,
    LISTA_IMAGEN;

    /** Indica si este tipo representa una lista de elementos. */
    public boolean esLista() {
        return this == LISTA_TEXTO || this == LISTA_BINARIO
                || this == LISTA_BASE64 || this == LISTA_IMAGEN;
    }

    /** Devuelve el tipo lista equivalente al tipo simple. */
    public TipoDato aLista() {
        switch (this) {
            case TEXTO:   return LISTA_TEXTO;
            case BINARIO: return LISTA_BINARIO;
            case BASE64:  return LISTA_BASE64;
            case IMAGEN:  return LISTA_IMAGEN;
            default:      return this;
        }
    }

    /** Devuelve el tipo simple equivalente al tipo lista. */
    public TipoDato aSingle() {
        switch (this) {
            case LISTA_TEXTO:   return TEXTO;
            case LISTA_BINARIO: return BINARIO;
            case LISTA_BASE64:  return BASE64;
            case LISTA_IMAGEN:  return IMAGEN;
            default:            return this;
        }
    }
}
