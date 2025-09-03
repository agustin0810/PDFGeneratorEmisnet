package com.bmv.emisnet.pdfgenerator.service;

import com.bmv.emisnet.pdfgenerator.model.ReportePosicionesTest;
import com.bmv.emisnet.pdfgenerator.model.AvisoExtemporaneidadTest;
import com.bmv.emisnet.pdfgenerator.model.ConfirmacionEnvioTest;
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
import java.util.Arrays;
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
        // Crear objeto de prueba con la estructura correcta para la plantilla
        ReportePosicionesTest reporte = new ReportePosicionesTest();
        reporte.setFechaOperacion(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reporte.setCasaBolsa("ACTIN");
        reporte.setRazonSocial("ACTINVER CASA DE BOLSA, S.A. DE C.V.");
        
        // Crear grupo de posiciones
        ReportePosicionesTest.GrupoPosiciones grupo = new ReportePosicionesTest.GrupoPosiciones();
        grupo.setNombre("Grupo de Valores Gubernamentales");
        
        // Crear posiciones con los mismos datos originales
        List<ReportePosicionesTest.PosicionDetalle> posiciones = new ArrayList<>();
        
        // Datos basados en la imagen proporcionada originalmente
        String[] emisoras = {"WC", "ESGMEXISHRS", "FEMSA", "FIBRAM", "NAFTRA", "SMARTR", "WALMEX"};
        String[] series = {"1", "1B", "UBD", "12", "ISHRS", "14", "1"};
        String[] tvs = {"1", "1B", "CF", "1", "1", "1", "1"};
        int[] saldosIniciales = {33388, 2300, 2302, 10500, 533200, 15900, 9955};
        int[] saldosAnterioresVct = {33388, 2300, 2302, 10500, 533200, 15900, 9655};
        int[] posicionesVct = {33388, 2300, 2302, 10500, 533200, 15900, 9666};
        int[] posicionesVcp = {0, 0, 0, 0, 0, 0, 300};
        int[] posicionesTotal = {33388, 2300, 2302, 10500, 533200, 15900, 9966};
        
        for (int i = 0; i < 7; i++) {
            ReportePosicionesTest.PosicionDetalle posicion = new ReportePosicionesTest.PosicionDetalle();
            
            // EMISIÓN
            posicion.setEmisora(emisoras[i]);
            posicion.setSerie(series[i]);
            posicion.setTv(tvs[i]);
            
            // SALDO ANTERIOR
            posicion.setSaldoInicial(saldosIniciales[i]);
            posicion.setSaldoAnteriorVcp(posicionesVcp[i]);
            posicion.setSaldoAnteriorVct(saldosAnterioresVct[i]);
            posicion.setSaldoAnteriorCto(0);
            
            // MONTO OPERADO
            posicion.setMontoOperadoVcp(0);
            posicion.setMontoOperadoVct(0);
            posicion.setMontoOperadoCto(0);
            posicion.setMontoOperadoTotal(0);
            
            // MONTO CANCELADO
            posicion.setMontoCanceladoVcp(0);
            posicion.setMontoCanceladoVct(0);
            posicion.setMontoCanceladoCto(0);
            posicion.setMontoCanceladoTotal(0);
            
            // MONTO MODIFICADO
            posicion.setMontoModificadoVcp(0);
            posicion.setMontoModificadoVct(0);
            posicion.setMontoModificadoCto(0);
            posicion.setMontoModificadoTotal(0);
            
            // POSICIÓN
            posicion.setPosicionVcp(posicionesVcp[i]);
            posicion.setPosicionVct(posicionesVct[i]);
            posicion.setPosicionCto(0);
            posicion.setPosicionTotal(posicionesTotal[i]);
            
            posiciones.add(posicion);
        }
        
        grupo.setPosiciones(posiciones);
        reporte.setGruposPosiciones(Arrays.asList(grupo));
        
        // Convertir objeto a mapa usando ObjectToMap
        Map<String, Object> datos = ObjectToMapConverter.convertToMap(reporte);
        
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
        System.out.println("  Archivo PDF guardado: " + archivoPDF.toString());
        System.out.println("  Tamaño PDF: " + pdfBytes.length + " bytes");
    }



    /**
     * Prueba unitaria: Generar aviso de extemporaneidad
     * Llama directamente al método generarPDF()
     */
    @Test
    void testGenerarAvisoExtemporaneidad() throws Exception {
        // Crear objeto de prueba con los mismos datos originales
        AvisoExtemporaneidadTest aviso = new AvisoExtemporaneidadTest();
        aviso.setFechaGeneracion(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        aviso.setClaveCotizacion("ACTINVER");
        aviso.setRazonSocial("ACTINVER CASA DE BOLSA, S.A. DE C.V.");
        aviso.setTipoInformacion("Información financiera trimestral");
        aviso.setCausasIncumplimiento("Problemas técnicos en el sistema de reportes que impidieron el envío oportuno de la información requerida.");
        aviso.setObservaciones("Se realizará el envío tan pronto como se resuelvan los problemas técnicos identificados.");
        
        // Convertir objeto a mapa usando ObjectToMap
        Map<String, Object> datos = ObjectToMapConverter.convertToMap(aviso);
        
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
        // Crear objeto de prueba con los mismos datos originales
        ConfirmacionEnvioTest confirmacion = new ConfirmacionEnvioTest();
        confirmacion.setFechaHoraEnvio(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " 10:30:00");
        confirmacion.setClave("ACTINVER");
        confirmacion.setRazonSocial("ACTINVER CASA DE BOLSAA, S.A. DE C.V.");
        confirmacion.setFolioRecepcion("14529044");
        confirmacion.setResponsable("ACTINVER EQUITY Peyrani");
        confirmacion.setPeriodo("Ejercicio 2025-02");
        
        // Crear archivos de ejemplo
        ConfirmacionEnvioTest.ArchivoRecibido archivo1 = new ConfirmacionEnvioTest.ArchivoRecibido();
        archivo1.setNombre("constrim.pdf");
        archivo1.setDescripcion("Constancia Trimestral");
        archivo1.setTamano(1024000L);
        archivo1.setTipoArchivo("PDF");
        
        ConfirmacionEnvioTest.ArchivoRecibido archivo2 = new ConfirmacionEnvioTest.ArchivoRecibido();
        archivo2.setNombre("reporte_mensual.pdf");
        archivo2.setDescripcion("Reporte Mensual de Operaciones");
        archivo2.setTamano(2048000L);
        archivo2.setTipoArchivo("PDF");
        
        confirmacion.setArchivos(Arrays.asList(archivo1, archivo2));
        
        // Convertir objeto a mapa usando ObjectToMap
        Map<String, Object> datos = ObjectToMapConverter.convertToMap(confirmacion);
        
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
    
}
