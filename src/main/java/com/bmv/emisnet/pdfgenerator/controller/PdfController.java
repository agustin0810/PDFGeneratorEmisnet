package com.bmv.emisnet.pdfgenerator.controller;

import com.bmv.emisnet.pdfgenerator.model.DatosReporte;
import com.bmv.emisnet.pdfgenerator.model.Venta;
import com.bmv.emisnet.pdfgenerator.service.GeneradorReportesPDF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * PDF Controller
 * 
 * REST controller for handling PDF generation requests using HTML templates.
 * Provides endpoints for generating different types of PDF reports.
 */
@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
@Slf4j
public class PdfController {

    private final GeneradorReportesPDF generadorReportesPDF;

    /**
     * Genera un reporte de ventas en PDF usando plantillas HTML
     * 
     * @return PDF del reporte de ventas como recurso descargable
     */
    @GetMapping("/reporte-ventas")
    public ResponseEntity<Resource> generarReporteVentas() {
        log.info("Generando reporte de ventas");
        
        try {
            // Obtener datos de muestra (en producción vendría de BD/servicio)
            DatosReporte datos = obtenerDatosVentas();
            
            // Generar PDF
            String nombreArchivo = "reporte-ventas-" + LocalDate.now() + ".pdf";
            String rutaTemporal = System.getProperty("java.io.tmpdir") + "/" + nombreArchivo;
            
            generadorReportesPDF.generarReporteVentas(datos, rutaTemporal);
            
            // Retornar archivo
            Resource resource = new FileSystemResource(rutaTemporal);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
                
        } catch (Exception e) {
            log.error("Error generando reporte de ventas", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Test simple de generación de PDF
     */
    @GetMapping("/test")
    public ResponseEntity<Resource> testPdf() {
        log.info("Generando PDF de test");
        
        try {
            String nombreArchivo = "test-simple.pdf";
            String rutaTemporal = System.getProperty("java.io.tmpdir") + "/" + nombreArchivo;
            
            // Generar PDF simple
            generadorReportesPDF.generarPdfSimple(rutaTemporal);
            
            Resource resource = new FileSystemResource(rutaTemporal);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
                
        } catch (Exception e) {
            log.error("Error generando PDF de test", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Genera un PDF de confirmación de envío con el nuevo diseño moderno
     * 
     * @return PDF de confirmación de envío como recurso descargable
     */
    @GetMapping("/confirmacion-envio")
    public ResponseEntity<Resource> generarConfirmacionEnvio() {
        log.info("Generando PDF de confirmación de envío");
        
        try {
            String nombreArchivo = "confirmacion-envio-" + LocalDate.now() + ".pdf";
            String rutaTemporal = System.getProperty("java.io.tmpdir") + "/" + nombreArchivo;
            
            // Generar PDF con el nuevo template
            generadorReportesPDF.generarConfirmacionEnvio(rutaTemporal);
            
            Resource resource = new FileSystemResource(rutaTemporal);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
                
        } catch (Exception e) {
            log.error("Error generando PDF de confirmación de envío", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Health check endpoint
     * 
     * @return Simple health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("PDF Generator Service is running");
    }
    
    /**
     * Obtiene datos de muestra para el reporte
     * En un entorno real, esto vendría de una base de datos o servicio externo
     */
    private DatosReporte obtenerDatosVentas() {
        List<Venta> ventas = Arrays.asList(
            new Venta(LocalDate.of(2024, 1, 15), "BBVA Bancomer", "Acciones AMXL", 100, 25.50, 2550.00),
            new Venta(LocalDate.of(2024, 1, 16), "Banorte", "Acciones FEMSA", 50, 89.75, 4487.50),
            new Venta(LocalDate.of(2024, 1, 17), "Santander", "Acciones WALMEX", 75, 42.30, 3172.50),
            new Venta(LocalDate.of(2024, 1, 18), "HSBC", "Acciones GFNORTE", 30, 156.80, 4704.00),
            new Venta(LocalDate.of(2024, 1, 19), "Citibanamex", "Acciones CEMEX", 200, 8.45, 1690.00),
            new Venta(LocalDate.of(2024, 1, 20), "Scotia Bank", "Acciones TLEVISA", 85, 12.60, 1071.00),
            new Venta(LocalDate.of(2024, 1, 21), "Inbursa", "Acciones BIMBO", 120, 36.25, 4350.00),
            new Venta(LocalDate.of(2024, 1, 22), "Afirme", "Acciones ELEKTRA", 40, 785.20, 31408.00)
        );
        
        return new DatosReporte("Enero 2024", ventas);
    }
}