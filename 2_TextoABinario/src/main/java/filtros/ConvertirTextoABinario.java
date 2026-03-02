package filtros;

import datos.PaqueteDatos;
import datos.TipoDato;
import interfaces.IFiltro;

/**
 * ID2 - ConvertirTextoABinario
 *
 * Convierte cada byte del texto a su representación en bits (8 dígitos).
 * Ejemplo: "Hi" → "01001000 01101001"
 *
 * IN:  TEXTO   → bytes con contenido textual
 * OUT: BINARIO → bytes con la cadena de bits (separados por espacio)
 *
 * Conexiones válidas después: ID4 (ConvertirBinarioABase64)
 */
public class ConvertirTextoABinario implements IFiltro {

    @Override
    public PaqueteDatos procesar(PaqueteDatos entrada) {
        byte[] texto = entrada.getDatos();
        System.out.println("  [ID2] Convirtiendo " + texto.length + " bytes de texto a binario...");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length; i++) {
            // Cada byte → cadena de 8 bits (relleno con ceros a la izquierda)
            sb.append(String.format("%8s", Integer.toBinaryString(texto[i] & 0xFF))
                    .replace(' ', '0'));
            if (i < texto.length - 1) sb.append(' ');
        }

        byte[] resultado = sb.toString().getBytes();
        System.out.println("  [ID2] Representación binaria: " + resultado.length + " bytes.");
        return new PaqueteDatos(TipoDato.BINARIO, resultado);
    }

    @Override public int      getId()          { return 2; }
    @Override public String   getNombre()       { return "ConvertirTextoABinario"; }
    @Override public String   getDescripcion()  { return "Convierte texto a su representación binaria (cadena de bits)."; }
    @Override public TipoDato getTipoEntrada()  { return TipoDato.TEXTO; }
    @Override public TipoDato getTipoSalida()   { return TipoDato.BINARIO; }
}
