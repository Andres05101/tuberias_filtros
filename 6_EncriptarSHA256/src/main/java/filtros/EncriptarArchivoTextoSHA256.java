package filtros;

import datos.PaqueteDatos;
import datos.TipoDato;
import interfaces.IFiltro;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ID6 - EncriptarArchivoTextoSHA256
 *
 * Aplica el algoritmo de hash SHA-256 al contenido de texto de entrada
 * y devuelve la representación hexadecimal del hash resultante.
 *
 * IN:  TEXTO → bytes con contenido textual
 * OUT: TEXTO → bytes con el hash SHA-256 en formato hexadecimal (64 chars)
 *
 * Usa java.security.MessageDigest (built-in Java). Sin dependencias externas.
 *
 * Nota: SHA-256 es una función de hash unidireccional, no cifrado reversible.
 */
public class EncriptarArchivoTextoSHA256 implements IFiltro {

    @Override
    public PaqueteDatos procesar(PaqueteDatos entrada) {
        byte[] texto = entrada.getDatos();
        System.out.println("  [ID6] Aplicando SHA-256 a " + texto.length + " bytes...");

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(texto);

            // Convertir bytes a cadena hexadecimal (64 caracteres)
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }

            String hashHex = hex.toString();
            System.out.println("  [ID6] SHA-256: " + hashHex);
            return new PaqueteDatos(TipoDato.TEXTO, hashHex.getBytes());

        } catch (NoSuchAlgorithmException e) {
            // SHA-256 está garantizado en Java SE — nunca debería ocurrir
            System.err.println("  [ID6] ERROR crítico: SHA-256 no disponible → " + e.getMessage());
            return new PaqueteDatos(TipoDato.TEXTO, new byte[0]);
        }
    }

    @Override public int      getId()          { return 6; }
    @Override public String   getNombre()       { return "EncriptarArchivoTextoSHA256"; }
    @Override public String   getDescripcion()  { return "Genera el hash SHA-256 del contenido de texto (resultado en hex)."; }
    @Override public TipoDato getTipoEntrada()  { return TipoDato.TEXTO; }
    @Override public TipoDato getTipoSalida()   { return TipoDato.TEXTO; }
}
