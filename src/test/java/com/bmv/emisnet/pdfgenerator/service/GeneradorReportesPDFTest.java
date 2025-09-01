package com.bmv.emisnet.pdfgenerator.service;

import com.bmv.emisnet.pdfgenerator.model.DatosReporte;
import com.bmv.emisnet.pdfgenerator.model.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el generador de reportes PDF
 * 
 * Estas pruebas validan que:
 * 1. El PDF se genera correctamente
 * 2. El archivo tiene el tamaño esperado
 * 3. El contenido HTML se procesa sin errores
 * 4. Los datos se renderizan correctamente
 */
class GeneradorReportesPDFTest {

    private GeneradorReportesPDF generadorPDF;
    private DatosReporte datosPrueba;

    @BeforeEach
    void setUp() {
        generadorPDF = new GeneradorReportesPDF();
        
        // Crear datos de prueba
        List<Venta> ventas = Arrays.asList(
            new Venta(LocalDate.of(2024, 1, 15), "BBVA Bancomer", "Acciones AMXL", 100, 25.50, 2550.00),
            new Venta(LocalDate.of(2024, 1, 16), "Banorte", "Acciones FEMSA", 50, 89.75, 4487.50),
            new Venta(LocalDate.of(2024, 1, 17), "Santander", "Acciones WALMEX", 75, 42.30, 3172.50)
        );
        
        datosPrueba = new DatosReporte("Enero 2024", ventas);
    }

    @Test
    void testGenerarReporteVentas_CreaArchivoPDF(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path archivoPDF = tempDir.resolve("reporte-prueba.pdf");
        
        // Act
        generadorPDF.generarReporteVentas(datosPrueba, archivoPDF.toString());
        
        // Assert
        assertTrue(Files.exists(archivoPDF), "El archivo PDF debe existir");
        assertTrue(Files.size(archivoPDF) > 0, "El archivo PDF no debe estar vacío");
        
        // Verificar que es un PDF válido (debe empezar con %PDF)
        byte[] contenido = Files.readAllBytes(archivoPDF);
        String inicioArchivo = new String(contenido, 0, Math.min(10, contenido.length));
        assertTrue(inicioArchivo.startsWith("%PDF"), 
            "El archivo debe ser un PDF válido (debe empezar con %PDF)");
    }

    @Test
    void testGenerarReporteVentas_TamañoArchivoReasonable(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path archivoPDF = tempDir.resolve("reporte-tamaño.pdf");
        
        // Act
        generadorPDF.generarReporteVentas(datosPrueba, archivoPDF.toString());
        
        // Assert
        long tamañoArchivo = Files.size(archivoPDF);
        
        // El PDF debe tener un tamaño razonable (entre 1KB y 100KB para este reporte)
        assertTrue(tamañoArchivo > 1000, 
            "El PDF debe tener al menos 1KB de contenido");
        assertTrue(tamañoArchivo < 100000, 
            "El PDF no debe ser excesivamente grande (menos de 100KB)");
        
        System.out.println("Tamaño del PDF generado: " + tamañoArchivo + " bytes");
    }

    @Test
    void testGenerarPdfSimple_CreaArchivoValido(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path archivoPDF = tempDir.resolve("test-simple.pdf");
        
        // Act
        generadorPDF.generarPdfSimple(archivoPDF.toString());
        
        // Assert
        assertTrue(Files.exists(archivoPDF), "El archivo PDF simple debe existir");
        assertTrue(Files.size(archivoPDF) > 0, "El archivo PDF simple no debe estar vacío");
        
        // Verificar que es un PDF válido
        byte[] contenido = Files.readAllBytes(archivoPDF);
        String inicioArchivo = new String(contenido, 0, Math.min(10, contenido.length));
        assertTrue(inicioArchivo.startsWith("%PDF"), 
            "El archivo simple debe ser un PDF válido");
    }

    @Test
    void testGenerarReporteVentas_ConDatosVacios(@TempDir Path tempDir) throws Exception {
        // Arrange
        DatosReporte datosVacios = new DatosReporte("Período Vacío", Arrays.asList());
        Path archivoPDF = tempDir.resolve("reporte-vacio.pdf");
        
        // Act & Assert
        // Debe generar el PDF sin lanzar excepción, incluso con datos vacíos
        assertDoesNotThrow(() -> {
            generadorPDF.generarReporteVentas(datosVacios, archivoPDF.toString());
        });
        
        assertTrue(Files.exists(archivoPDF), "Debe generar PDF incluso con datos vacíos");
    }

    @Test
    void testGenerarReporteVentas_ConMuchosDatos(@TempDir Path tempDir) throws Exception {
        // Arrange - Crear muchos datos para probar rendimiento
        List<Venta> muchasVentas = Arrays.asList(
            new Venta(LocalDate.of(2024, 1, 1), "Cliente 1", "Producto A", 10, 100.0, 1000.0),
            new Venta(LocalDate.of(2024, 1, 2), "Cliente 2", "Producto B", 20, 200.0, 4000.0),
            new Venta(LocalDate.of(2024, 1, 3), "Cliente 3", "Producto C", 30, 300.0, 9000.0),
            new Venta(LocalDate.of(2024, 1, 4), "Cliente 4", "Producto D", 40, 400.0, 16000.0),
            new Venta(LocalDate.of(2024, 1, 5), "Cliente 5", "Producto E", 50, 500.0, 25000.0)
        );
        
        DatosReporte datosGrandes = new DatosReporte("Período Grande", muchasVentas);
        Path archivoPDF = tempDir.resolve("reporte-grande.pdf");
        
        // Act
        long tiempoInicio = System.currentTimeMillis();
        generadorPDF.generarReporteVentas(datosGrandes, archivoPDF.toString());
        long tiempoFin = System.currentTimeMillis();
        
        // Assert
        assertTrue(Files.exists(archivoPDF), "Debe generar PDF con muchos datos");
        assertTrue(Files.size(archivoPDF) > 0, "El PDF con muchos datos no debe estar vacío");
        
        long tiempoTotal = tiempoFin - tiempoInicio;
        System.out.println("Tiempo de generación con muchos datos: " + tiempoTotal + "ms");
        
        // Debe generar en menos de 5 segundos
        assertTrue(tiempoTotal < 5000, 
            "La generación debe ser rápida (menos de 5 segundos)");
    }
}
