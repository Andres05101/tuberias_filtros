package inicio;

import fabrica.FabricaFiltros;
import motor.GestorTuberias;
import presentacion.VistaEscritorio;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación Tuberías y Filtros.
 *
 * Responsabilidad ÚNICA: bootstrap (arranque).
 *   - NO instancia filtros ni componentes directamente.
 *   - Delega TODA la creación de objetos a FabricaFiltros (Singleton).
 *   - Crea e inicia la VistaEscritorio (Presentación).
 *
 * Flujo de control:
 *   1. Obtener FabricaFiltros (Singleton — transversal).
 *   2. Pedir a la fábrica que cree el GestorTuberias (con los 8 filtros).
 *   3. Crear la VistaEscritorio con inyección de dependencias por constructor.
 *   4. Mostrar la vista en el Event Dispatch Thread de Swing.
 *
 * Principios SOLID:
 *   - Single Responsibility: solo orquesta el arranque.
 *   - Dependency Inversion: depende de FabricaFiltros, no de clases concretas.
 *
 * Igual que InicioAplicacion en ComponentesConCapas.
 */
public class InicioAplicacion {

    public static void main(String[] args) {

        // ── Paso 1: Obtener la FabricaFiltros (Singleton transversal) ─
        FabricaFiltros fabrica = FabricaFiltros.getInstance();

        // ── Paso 2: Crear el GestorTuberias vía fábrica ───────────────
        // La fábrica construye los 8 filtros e inyecta sus dependencias.
        GestorTuberias gestor = fabrica.crearGestorTuberias();

        // ── Paso 3: Crear la VistaEscritorio con inyección por constructor
        // Inicio inyecta la fábrica y el gestor → la vista no necesita crearlos.
        VistaEscritorio vista = new VistaEscritorio(fabrica, gestor);

        // ── Paso 3.5: Garantizar que la carpeta output/ existe ────────
        java.io.File outputDir = new java.io.File("output");
        if (!outputDir.exists()) {
            boolean creada = outputDir.mkdirs();
            System.out.println(creada
                    ? "✔ Carpeta output/ creada en: " + outputDir.getAbsolutePath()
                    : "✘ No se pudo crear la carpeta output/.");
        } else {
            System.out.println("✔ Carpeta output/ encontrada en: " + outputDir.getAbsolutePath());
        }

        // ── Paso 4: Establecer Look & Feel y mostrar ventana ──────────
        // Forzar L&F multiplataforma (Metal) para que los colores de los
        // botones se rendericen correctamente en macOS.
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> {
            vista.mostrar();
        });
    }
}
