package com.bmv.emisnet.pdfgenerator.service;

import com.bmv.emisnet.pdfgenerator.model.DatosReporte;
import com.bmv.emisnet.pdfgenerator.model.Venta;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para generar reportes PDF usando plantillas HTML con Thymeleaf
 * y conversión a PDF con OpenPDF/Flying Saucer
 */
@Service
@Slf4j
public class GeneradorReportesPDF {
    
    private final TemplateEngine templateEngine;
    
    public GeneradorReportesPDF() {
        // Configurar Thymeleaf
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }
    
    /**
     * Genera un reporte de ventas en formato PDF
     *
     * @param datos los datos del reporte
     * @param rutaDestino ruta donde guardar el PDF generado
     * @throws IOException si hay error de I/O
     * @throws DocumentException si hay error en la generación del PDF
     */
    public void generarReporteVentas(DatosReporte datos, String rutaDestino)
           throws IOException, DocumentException {
        
        log.info("Generando reporte de ventas para período: {}", datos.getPeriodo());
        
        try {
            // 1. Preparar contexto con datos
            Context context = new Context();
            context.setVariable("titulo", "Reporte de Ventas Mensual");
            context.setVariable("periodo", datos.getPeriodo());
            context.setVariable("fechaGeneracion", LocalDate.now());
            
            // Información empresa
            Map<String, String> empresa = new HashMap<>();
            empresa.put("nombre", "BMV - Bolsa Mexicana de Valores");
            empresa.put("direccion", "Paseo de la Reforma 255, Ciudad de México");
            empresa.put("telefono", "+52-55-5342-9000");
            context.setVariable("empresa", empresa);
            
            // Resumen
            context.setVariable("resumen", calcularResumen(datos.getVentas()));
            
            // Lista de ventas
            context.setVariable("ventas", datos.getVentas());
            
            // Datos para gráfico
            context.setVariable("incluirGrafico", true);
            context.setVariable("ventasPorMes", calcularVentasPorMes(datos.getVentas()));
            
            // 2. Procesar plantilla HTML
            log.info("Procesando plantilla HTML...");
            String htmlContent = templateEngine.process("reporte-ventas", context);
            
            log.info("HTML generado exitosamente, longitud: {}", htmlContent.length());
            log.debug("HTML generado: {}", htmlContent.substring(0, Math.min(200, htmlContent.length())));
            
            // 3. Convertir HTML a PDF
            log.info("Convirtiendo HTML a PDF...");
            convertirHtmlAPdf(htmlContent, rutaDestino);
            
            log.info("Reporte PDF generado exitosamente en: {}", rutaDestino);
            
        } catch (Exception e) {
            log.error("Error generando reporte PDF", e);
            throw e;
        }
    }
    
    /**
     * Genera un PDF simple para testing
     */
    public void generarPdfSimple(String rutaDestino) throws IOException, DocumentException {
        log.info("Generando PDF simple de test");
        
        String htmlSimple = "<!DOCTYPE html><html><head><meta charset='UTF-8'/></head><body><h1>Test PDF</h1><p>Este es un test simple.</p></body></html>";
        
        convertirHtmlAPdf(htmlSimple, rutaDestino);
        
        log.info("PDF simple generado en: {}", rutaDestino);
    }
    
    /**
     * Convierte contenido HTML a PDF usando Flying Saucer y OpenPDF
     */
    private void convertirHtmlAPdf(String htmlContent, String rutaDestino)
           throws IOException, DocumentException {
        
        try (OutputStream outputStream = new FileOutputStream(rutaDestino)) {
            ITextRenderer renderer = new ITextRenderer();
            
            // Configurar documento HTML
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            log.debug("PDF creado exitosamente");
        }
    }
    
    /**
     * Calcula el resumen ejecutivo del reporte
     */
    private Map<String, Object> calcularResumen(List<Venta> ventas) {
        double total = ventas.stream().mapToDouble(Venta::getTotal).sum();
        int transacciones = ventas.size();
        double promedio = transacciones > 0 ? total / transacciones : 0;
        
        return Map.of(
            "totalVentas", String.format("$%.2f", total),
            "numeroTransacciones", transacciones,
            "ventaPromedio", String.format("$%.2f", promedio)
       );
    }
    
    /**
     * Calcula datos para el gráfico de ventas por mes
     */
    private List<Map<String, Object>> calcularVentasPorMes(List<Venta> ventas) {
        // Agrupar ventas por mes y calcular totales
        Map<String, Double> ventasPorMes = ventas.stream()
            .collect(Collectors.groupingBy(
                venta -> venta.getFecha().getMonth().name(),
                Collectors.summingDouble(Venta::getTotal)
            ));
        
        double maxVenta = ventasPorMes.values().stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElse(1);
        
        return ventasPorMes.entrySet().stream()
            .map(entry -> {
                Map<String, Object> mesData = new HashMap<>();
                mesData.put("nombre", entry.getKey());
                mesData.put("total", entry.getValue());
                mesData.put("porcentaje", (entry.getValue() / maxVenta) * 100);
                return mesData;
            })
            .collect(Collectors.toList());
    }
}
