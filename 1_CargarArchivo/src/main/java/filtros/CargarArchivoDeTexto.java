package filtros;

import datos.PaqueteDatos;
import datos.TipoDato;
import interfaces.IFiltro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ID1 - CargarArchivoDeTexto
 *
 * Lee el contenido de un archivo de texto desde el sistema de archivos.
 *
 * IN:  PATH  → bytes que contienen la ruta del archivo (String)
 * OUT: TEXTO → bytes con el contenido completo del archivo
 *
 * Primer filtro típico de una tubería: carga el dato inicial.
 */
public class CargarArchivoDeTexto implements IFiltro {

    @Override
    public PaqueteDatos procesar(PaqueteDatos entrada) {
        String ruta = new String(entrada.getDatos()).trim();
        System.out.println("  [ID1] Cargando archivo: " + ruta);
        try {
            byte[] contenido = Files.readAllBytes(Paths.get(ruta));
            System.out.println("  [ID1] Archivo cargado: " + contenido.length + " bytes.");
            return new PaqueteDatos(TipoDato.TEXTO, contenido);
        } catch (IOException e) {
            System.err.println("  [ID1] ERROR: No se pudo leer el archivo → " + e.getMessage());
            return new PaqueteDatos(TipoDato.TEXTO, new byte[0]);
        }
    }

    @Override public int      getId()          { return 1; }
    @Override public String   getNombre()       { return "CargarArchivoDeTexto"; }
    @Override public String   getDescripcion()  { return "Lee el contenido de un archivo de texto del sistema de archivos."; }
    @Override public TipoDato getTipoEntrada()  { return TipoDato.PATH; }
    @Override public TipoDato getTipoSalida()   { return TipoDato.TEXTO; }
}
