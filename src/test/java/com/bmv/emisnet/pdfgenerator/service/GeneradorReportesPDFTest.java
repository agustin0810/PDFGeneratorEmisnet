package com.bmv.emisnet.pdfgenerator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el servicio GeneradorReportesPDF
 * 
 * Estas pruebas verifican la generación de PDFs para cada plantilla
 * incluida en el sistema, llamando directamente a los métodos del servicio.
 * Los PDFs generados se guardan en target/generated-pdfs/ para inspección.
 * La orientación se define en cada plantilla HTML con CSS.
 */
class GeneradorReportesPDFTest {

    private GeneradorReportesPDF generadorPDF;
    private Path outputDir;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Crear instancia del servicio directamente
        generadorPDF = new GeneradorReportesPDF();
        
        // Crear directorio de salida para los PDFs
        outputDir = Paths.get("target", "generated-pdfs");
        Files.createDirectories(outputDir);
    }

    /**
     * Prueba unitaria: Generar PDF de reporte de posiciones
     * Llama directamente al método generarPDF()
     */
    @Test
    void testGenerarReportePosiciones() throws Exception {
        // Preparar datos para la plantilla
        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", "Reporte de Posiciones");
        datos.put("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datos.put("posiciones", crearDatosPosiciones());
        datos.put("totalPosiciones", 5);
        
        // Llamar directamente al método del servicio
        byte[] pdfBytes = generadorPDF.generarPDF("reporte-posiciones", datos);
        
        // Verificar que se generaron bytes
        assertNotNull(pdfBytes, "El array de bytes no debe ser null");
        assertTrue(pdfBytes.length > 0, "El array de bytes no debe estar vacío");
        
        // Guardar el PDF generado para inspección
        Path archivoPDF = outputDir.resolve("reporte-posiciones.pdf");
        Files.write(archivoPDF, pdfBytes);
        
        System.out.println("✓ Prueba unitaria reporte-posiciones: EXITOSA");
        System.out.println("  Método llamado: generarPDF()");
        System.out.println("  Archivo guardado: " + archivoPDF.toString());
        System.out.println("  Tamaño: " + pdfBytes.length + " bytes");
    }

    /**
     * Prueba unitaria: Generar PDF de reporte de ventas
     * Llama directamente al método generarPDF()
     */
    @Test
    void testGenerarReporteVentas() throws Exception {
        // Preparar datos para la plantilla
        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", "Reporte de Ventas");
        datos.put("periodo", "Enero 2024");
        datos.put("fechaGeneracion", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Datos de empresa
        Map<String, Object> empresa = new HashMap<>();
        empresa.put("nombre", "BMV - Bolsa Mexicana de Valores");
        empresa.put("direccion", "Av. Paseo de la Reforma 255, Col. Cuauhtémoc, CDMX");
        empresa.put("telefono", "55-1234-5678");
        datos.put("empresa", empresa);
        
        // Datos de resumen
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalVentas", "$150,000.00");
        resumen.put("numeroTransacciones", "25");
        resumen.put("ventaPromedio", "$6,000.00");
        datos.put("resumen", resumen);
        
        // Datos de ventas
        datos.put("ventas", crearDatosVentasCompletos());
        
        // Llamar directamente al método del servicio
        byte[] pdfBytes = generadorPDF.generarPDF("reporte-ventas", datos);
        
        // Verificar que se generaron bytes
        assertNotNull(pdfBytes, "El array de bytes no debe ser null");
        assertTrue(pdfBytes.length > 0, "El array de bytes no debe estar vacío");
        
        // Guardar el PDF generado para inspección
        Path archivoPDF = outputDir.resolve("reporte-ventas.pdf");
        Files.write(archivoPDF, pdfBytes);
        
        System.out.println("✓ Prueba unitaria reporte-ventas: EXITOSA");
        System.out.println("  Método llamado: generarPDF()");
        System.out.println("  Archivo guardado: " + archivoPDF.toString());
        System.out.println("  Tamaño: " + pdfBytes.length + " bytes");
    }

    /**
     * Prueba unitaria: Generar aviso de extemporaneidad
     * Llama directamente al método generarPDF()
     */
    @Test
    void testGenerarAvisoExtemporaneidad() throws Exception {
        // Preparar datos para la plantilla
        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", "Aviso de Extemporaneidad");
        datos.put("mensaje", "Se informa que el envío de información se realizó fuera del horario establecido.");
        datos.put("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datos.put("hora", "15:30");
        
        // Llamar directamente al método del servicio
        byte[] pdfBytes = generadorPDF.generarPDF("aviso-extemporaneidad", datos);
        
        // Verificar que se generaron bytes
        assertNotNull(pdfBytes, "El array de bytes no debe ser null");
        assertTrue(pdfBytes.length > 0, "El array de bytes no debe estar vacío");
        
        // Guardar el PDF generado para inspección
        Path archivoPDF = outputDir.resolve("aviso-extemporaneidad.pdf");
        Files.write(archivoPDF, pdfBytes);
        
        System.out.println("✓ Prueba unitaria aviso-extemporaneidad: EXITOSA");
        System.out.println("  Método llamado: generarPDF()");
        System.out.println("  Archivo guardado: " + archivoPDF.toString());
        System.out.println("  Tamaño: " + pdfBytes.length + " bytes");
    }

    /**
     * Prueba unitaria: Generar confirmación de envío
     * Llama directamente al método generarPDF()
     */
    @Test
    void testGenerarConfirmacionEnvio() throws Exception {
        // Preparar datos para la plantilla
        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", "Confirmación de Envío");
        datos.put("numeroEnvio", "ENV-2024-001");
        datos.put("fechaEnvio", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datos.put("destinatario", "Sistema EMISNET");
        datos.put("estado", "Enviado exitosamente");
        
        // Llamar directamente al método del servicio
        byte[] pdfBytes = generadorPDF.generarPDF("confirmacion-envio", datos);
        
        // Verificar que se generaron bytes
        assertNotNull(pdfBytes, "El array de bytes no debe ser null");
        assertTrue(pdfBytes.length > 0, "El array de bytes no debe estar vacío");
        
        // Guardar el PDF generado para inspección
        Path archivoPDF = outputDir.resolve("confirmacion-envio.pdf");
        Files.write(archivoPDF, pdfBytes);
        
        System.out.println("✓ Prueba unitaria confirmacion-envio: EXITOSA");
        System.out.println("  Método llamado: generarPDF()");
        System.out.println("  Archivo guardado: " + archivoPDF.toString());
        System.out.println("  Tamaño: " + pdfBytes.length + " bytes");
    }

    /**
     * Prueba unitaria: Procesar plantilla HTML sin generar PDF
     * Llama directamente al método procesarPlantilla()
     */
    @Test
    void testProcesarPlantillaHTML() throws Exception {
        // Preparar datos para la plantilla
        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", "Prueba de Plantilla");
        datos.put("mensaje", "Este es un mensaje de prueba");
        datos.put("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Llamar directamente al método del servicio
        String htmlProcesado = generadorPDF.procesarPlantilla("aviso-extemporaneidad", datos);
        
        // Verificar que se procesó correctamente
        assertNotNull(htmlProcesado, "El HTML procesado no debe ser null");
        assertTrue(htmlProcesado.length() > 0, "El HTML procesado no debe estar vacío");
        assertTrue(htmlProcesado.contains("Aviso de Extemporaneidad"), "El HTML debe contener el título del documento");
        assertTrue(htmlProcesado.contains("Sistema Electrónico"), "El HTML debe contener el subtítulo");
        
        System.out.println("✓ Prueba unitaria procesar plantilla HTML: EXITOSA");
        System.out.println("  Método llamado: procesarPlantilla()");
        System.out.println("  Longitud del HTML: " + htmlProcesado.length() + " caracteres");
    }

    /**
     * Prueba unitaria: Manejo de errores con plantilla inexistente
     * Llama directamente al método generarPDF() con datos inválidos
     */
    @Test
    void testManejoErrorPlantillaInexistente() {
        // Preparar datos para la plantilla
        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", "Prueba de Error");
        
        // Intentar generar PDF con plantilla inexistente
        assertThrows(Exception.class, () -> {
            generadorPDF.generarPDF("plantilla-inexistente", datos);
        }, "Debe lanzar excepción con plantilla inexistente");
        
        System.out.println("✓ Prueba unitaria manejo de error: EXITOSA");
        System.out.println("  Método llamado: generarPDF() con plantilla inexistente");
    }

    /**
     * Crear datos de ejemplo para posiciones
     */
    private Map<String, Object>[] crearDatosPosiciones() {
        @SuppressWarnings("unchecked")
        Map<String, Object>[] posiciones = new Map[5];
        
        for (int i = 0; i < 5; i++) {
            Map<String, Object> posicion = new HashMap<>();
            posicion.put("emisor", "EMISOR-" + (i + 1));
            posicion.put("serie", "SERIE-" + (i + 1));
            posicion.put("cantidad", 1000 + (i * 100));
            posicion.put("valor", 50000.0 + (i * 10000.0));
            posiciones[i] = posicion;
        }
        
        return posiciones;
    }

    /**
     * Crear datos de ejemplo para ventas
     */
    private Map<String, Object>[] crearDatosVentas() {
        @SuppressWarnings("unchecked")
        Map<String, Object>[] ventas = new Map[3];
        
        for (int i = 0; i < 3; i++) {
            Map<String, Object> venta = new HashMap<>();
            venta.put("producto", "PRODUCTO-" + (i + 1));
            venta.put("cantidad", 10 + (i * 5));
            venta.put("precio", 100.0 + (i * 50.0));
            venta.put("total", (10 + (i * 5)) * (100.0 + (i * 50.0)));
            ventas[i] = venta;
        }
        
        return ventas;
    }

    /**
     * Crear datos de ejemplo para ventas con estructura completa
     */
    private Map<String, Object>[] crearDatosVentasCompletos() {
        @SuppressWarnings("unchecked")
        Map<String, Object>[] ventas = new Map[3];
        
        for (int i = 0; i < 3; i++) {
            Map<String, Object> venta = new HashMap<>();
            venta.put("fecha", LocalDate.now().minusDays(i));
            venta.put("cliente", "CLIENTE-" + (i + 1));
            venta.put("producto", "PRODUCTO-" + (i + 1));
            venta.put("cantidad", 10 + (i * 5));
            venta.put("precioUnitario", 100.0 + (i * 50.0));
            venta.put("total", (10 + (i * 5)) * (100.0 + (i * 50.0)));
            ventas[i] = venta;
        }
        
        return ventas;
    }

    @Test
    void testEjemploPlantilla() throws IOException {
        // Preparar datos de prueba
        Map<String, Object> datos = new HashMap<>();
        datos.put("fechaGeneracion", "02/09/2025");
        datos.put("usuario", "Usuario Ejemplo");
        datos.put("campo1", "Valor 1");
        datos.put("campo2", "Valor 2");
        datos.put("campo3", "Valor 3");
        
        // Crear lista de items de ejemplo
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", i);
            item.put("nombre", "Item " + i);
            item.put("valor", 1000 + i * 100);
            item.put("estado", i % 2 == 0 ? "Activo" : "Inactivo");
            items.add(item);
        }
        datos.put("items", items);

        // Generar PDF
        byte[] pdfBytes = generadorPDF.generarPDF("ejemplo-plantilla", datos);
        
        // Verificar que se generó correctamente
        assertNotNull(pdfBytes, "El PDF no debería ser null");
        assertTrue(pdfBytes.length > 0, "El PDF debería tener contenido");
        
        // Guardar PDF para verificación manual
        Path outputPath = Paths.get("target/generated-pdfs/ejemplo-plantilla.pdf");
        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, pdfBytes);
        
        System.out.println("✓ Prueba unitaria ejemplo-plantilla: EXITOSA");
        System.out.println("  Método llamado: generarPDF()");
        System.out.println("  Archivo guardado: " + outputPath.toString());
        System.out.println("  Tamaño: " + pdfBytes.length + " bytes");
    }
}
