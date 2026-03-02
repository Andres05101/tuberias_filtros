package filtros;

import datos.PaqueteDatos;
import datos.TipoDato;
import interfaces.IFiltro;

import java.util.Base64;

/**
 * ID5 - ConvertirBase64ABinario
 *
 * Decodifica datos codificados en Base64 y devuelve los bytes binarios originales.
 * Complemento inverso del Filtro ID4.
 *
 * IN:  BASE64  → bytes con contenido en Base64
 * OUT: BINARIO → bytes originales decodificados
 *
 * Si la entrada es LISTA_BASE64, toma el primer elemento.
 * Usa java.util.Base64 (built-in Java 8+).
 */
public class ConvertirBase64ABinario implements IFiltro {

    @Override
    public PaqueteDatos procesar(PaqueteDatos entrada) {
        byte[] base64Bytes;

        // Maneja tanto BASE64 simple como LISTA_BASE64 (toma el primero)
        if (entrada.esLista()) {
            System.out.println("  [ID5] Entrada es lista → tomando primer elemento.");
            PaqueteDatos primero = entrada.primerElemento();
            base64Bytes = primero.getDatos();
        } else {
            base64Bytes = entrada.getDatos();
        }

        System.out.println("  [ID5] Decodificando Base64 (" + base64Bytes.length + " bytes)...");
        try {
            byte[] decoded = Base64.getDecoder().decode(base64Bytes);
            System.out.println("  [ID5] Decodificado: " + decoded.length + " bytes binarios.");
            return new PaqueteDatos(TipoDato.BINARIO, decoded);
        } catch (IllegalArgumentException e) {
            System.err.println("  [ID5] ERROR: Datos Base64 inválidos → " + e.getMessage());
            return new PaqueteDatos(TipoDato.BINARIO, new byte[0]);
        }
    }

    @Override public int      getId()          { return 5; }
    @Override public String   getNombre()       { return "ConvertirBase64ABinario"; }
    @Override public String   getDescripcion()  { return "Decodifica contenido Base64 a bytes binarios originales."; }
    @Override public TipoDato getTipoEntrada()  { return TipoDato.BASE64; }
    @Override public TipoDato getTipoSalida()   { return TipoDato.BINARIO; }
}
