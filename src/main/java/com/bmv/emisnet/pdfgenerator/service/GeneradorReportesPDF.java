package com.bmv.emisnet.pdfgenerator.service;

import com.bmv.emisnet.pdfgenerator.model.DatosReporte;
import com.bmv.emisnet.pdfgenerator.model.Posicion;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
     * Genera un PDF de confirmación de envío con el nuevo diseño moderno
     *
     * @param rutaDestino ruta donde guardar el PDF generado
     * @throws IOException si hay error de I/O
     * @throws DocumentException si hay error en la generación del PDF
     */
    public void generarConfirmacionEnvio(String rutaDestino) throws IOException, DocumentException {
        log.info("Generando PDF de confirmación de envío");
        
        try {
            // 1. Preparar contexto con datos de muestra
            Context context = new Context();
            context.setVariable("fechaHora", "3/9/25, 9:11 a.m.");
            context.setVariable("clave", "ACTIN");
            context.setVariable("razonSocial", "ACTINVER CASA DE BOLSA, S.A. DE C.V.");
            context.setVariable("folioRecepcion", "1452904");
            context.setVariable("responsable", "ACTINVER EQUITY Peyrani");
            context.setVariable("fechaHoraEnvio", "2025-09-01 09:11:32.227");
            context.setVariable("periodo", "Ejercicio 2025-02");
            
            // Datos de archivos recibidos
            Map<String, String> archivo1 = new HashMap<>();
            archivo1.put("nombre", "constrim.pdf");
            archivo1.put("descripcion", "Constancia Trimestral");
            
            context.setVariable("archivos", List.of(archivo1));
            
            // 2. Procesar plantilla HTML
            log.info("Procesando plantilla de confirmación de envío...");
            String htmlContent = templateEngine.process("confirmacion-envio", context);
            
            log.info("HTML de confirmación generado exitosamente, longitud: {}", htmlContent.length());
            
            // 3. Convertir HTML a PDF
            log.info("Convirtiendo HTML a PDF...");
            convertirHtmlAPdf(htmlContent, rutaDestino);
            
            log.info("PDF de confirmación generado exitosamente en: {}", rutaDestino);
            
        } catch (Exception e) {
            log.error("Error generando PDF de confirmación", e);
            throw e;
        }
    }
    
    /**
     * Genera un PDF de aviso de extemporaneidad con el nuevo diseño
     *
     * @param rutaDestino ruta donde guardar el PDF generado
     * @throws IOException si hay error de I/O
     * @throws DocumentException si hay error en la generación del PDF
     */
    public void generarAvisoExtemporaneidad(String rutaDestino) throws IOException, DocumentException {
        log.info("Generando PDF de aviso de extemporaneidad");
        
        try {
            // 1. Preparar contexto con datos de muestra
            Context context = new Context();
            context.setVariable("fechaGeneracion", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            context.setVariable("claveCotizacion", "ACTIN");
            context.setVariable("razonSocial", "ACTINVER CASA DE BOLSA, S.A. DE C.V.");
            context.setVariable("tipoInformacion", "Constancia Trimestral de Operaciones");
            context.setVariable("causasIncumplimiento", "Retraso en la consolidación de información operativa. Se presentará el día 15/09/2025.");
            context.setVariable("observaciones", "Se ha implementado un nuevo sistema de reportes que ha requerido tiempo adicional para su validación.");
            
            // 2. Procesar plantilla HTML
            log.info("Procesando plantilla de aviso de extemporaneidad...");
            String htmlContent = templateEngine.process("aviso-extemporaneidad", context);
            
            log.info("HTML de aviso de extemporaneidad generado exitosamente, longitud: {}", htmlContent.length());
            
            // 3. Convertir HTML a PDF
            log.info("Convirtiendo HTML a PDF...");
            convertirHtmlAPdf(htmlContent, rutaDestino);
            
            log.info("PDF de aviso de extemporaneidad generado exitosamente en: {}", rutaDestino);
            
        } catch (Exception e) {
            log.error("Error generando PDF de aviso de extemporaneidad", e);
            throw e;
        }
    }
    
    /**
     * Genera un PDF de reporte de consulta de posiciones con el diseño moderno
     *
     * @param rutaDestino ruta donde guardar el PDF generado
     * @throws IOException si hay error de I/O
     * @throws DocumentException si hay error en la generación del PDF
     */
    public void generarReportePosiciones(String rutaDestino) throws IOException, DocumentException {
        log.info("Generando PDF de reporte de consulta de posiciones");
        
        try {
            // 1. Preparar contexto con datos de muestra
            Context context = new Context();
            context.setVariable("casaBolsa", "ACTIN");
            context.setVariable("razonSocial", "ACTINVER CASA DE BOLSA, S.A. DE C.V.");
            context.setVariable("fechaConsulta", "2025-01-15");
            context.setVariable("fechaOperacion", "1/9/2025");
            
            // Datos de posiciones de muestra basados en la imagen
            List<Posicion> posiciones = obtenerDatosPosiciones();
            context.setVariable("posiciones", posiciones);
            
            // 2. Procesar plantilla HTML
            log.info("Procesando plantilla de reporte de posiciones...");
            String htmlContent = templateEngine.process("reporte-posiciones", context);
            
            log.info("HTML de reporte de posiciones generado exitosamente, longitud: {}", htmlContent.length());
            
            // 3. Convertir HTML a PDF con orientación landscape
            log.info("Convirtiendo HTML a PDF en orientación landscape...");
            convertirHtmlAPdfLandscape(htmlContent, rutaDestino);
            
            log.info("PDF de reporte de posiciones generado exitosamente en: {}", rutaDestino);
            
        } catch (Exception e) {
            log.error("Error generando PDF de reporte de posiciones", e);
            throw e;
        }
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
     * Convierte contenido HTML a PDF en orientación landscape usando Flying Saucer y OpenPDF
     */
    private void convertirHtmlAPdfLandscape(String htmlContent, String rutaDestino)
           throws IOException, DocumentException {
        
        try (OutputStream outputStream = new FileOutputStream(rutaDestino)) {
            ITextRenderer renderer = new ITextRenderer();
            
            // Configurar documento HTML con orientación landscape
            // El CSS en la plantilla ya incluye @page { size: A4 landscape; }
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            log.debug("PDF landscape creado exitosamente");
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
    
    /**
     * Obtiene datos de muestra para el reporte de posiciones
     * Basado en la imagen proporcionada
     */
    private List<Posicion> obtenerDatosPosiciones() {
        List<Posicion> posiciones = Arrays.asList(
            crearPosicionWC(),
            crearPosicionESGMEXISHIRS(),
            crearPosicionFEMSAUBD(),
            crearPosicionFIBRAM12(),
            crearPosicionNAFTRAISHRS(),
            crearPosicionSMARTR14(),
            crearPosicionWALMEX()
        );
        
        return posiciones;
    }
    
    private Posicion crearPosicionWC() {
        Posicion posicion = new Posicion();
        posicion.setEmisor("WC");
        posicion.setSerie("1");
        posicion.setTv("");
        posicion.setSaldoInicial(33388.0);
        posicion.setSaldoAnteriorVcp(0.0);
        posicion.setSaldoAnteriorVct(33388.0);
        posicion.setSaldoAnteriorCto(0.0);
        posicion.setMontoOperadoVcp(0.0);
        posicion.setMontoOperadoVct(0.0);
        posicion.setMontoOperadoCto(0.0);
        posicion.setMontoOperadoTotal(0.0);
        posicion.setMontoCanceladoVcp(0.0);
        posicion.setMontoCanceladoVct(0.0);
        posicion.setMontoCanceladoCto(0.0);
        posicion.setMontoCanceladoTotal(0.0);
        posicion.setMontoModificadoVcp(0.0);
        posicion.setMontoModificadoVct(0.0);
        posicion.setMontoModificadoCto(0.0);
        posicion.setMontoModificadoTotal(0.0);
        posicion.setPosicionVcp(33388.0);
        posicion.setPosicionVct(0.0);
        posicion.setPosicionCto(33388.0);
        posicion.setPosicionTotal(33388.0);
        return posicion;
    }
    
    private Posicion crearPosicionESGMEXISHIRS() {
        Posicion posicion = new Posicion();
        posicion.setEmisor("ESGMEXISHIRS");
        posicion.setSerie("1B");
        posicion.setTv("");
        posicion.setSaldoInicial(2300.0);
        posicion.setSaldoAnteriorVcp(0.0);
        posicion.setSaldoAnteriorVct(2300.0);
        posicion.setSaldoAnteriorCto(0.0);
        posicion.setMontoOperadoVcp(0.0);
        posicion.setMontoOperadoVct(0.0);
        posicion.setMontoOperadoCto(0.0);
        posicion.setMontoOperadoTotal(0.0);
        posicion.setMontoCanceladoVcp(0.0);
        posicion.setMontoCanceladoVct(0.0);
        posicion.setMontoCanceladoCto(0.0);
        posicion.setMontoCanceladoTotal(0.0);
        posicion.setMontoModificadoVcp(0.0);
        posicion.setMontoModificadoVct(0.0);
        posicion.setMontoModificadoCto(0.0);
        posicion.setMontoModificadoTotal(0.0);
        posicion.setPosicionVcp(2300.0);
        posicion.setPosicionVct(0.0);
        posicion.setPosicionCto(2300.0);
        posicion.setPosicionTotal(2300.0);
        return posicion;
    }
    
    private Posicion crearPosicionFEMSAUBD() {
        Posicion posicion = new Posicion();
        posicion.setEmisor("FEMSA UBD");
        posicion.setSerie("1");
        posicion.setTv("");
        posicion.setSaldoInicial(2302.0);
        posicion.setSaldoAnteriorVcp(0.0);
        posicion.setSaldoAnteriorVct(2302.0);
        posicion.setSaldoAnteriorCto(0.0);
        posicion.setMontoOperadoVcp(0.0);
        posicion.setMontoOperadoVct(0.0);
        posicion.setMontoOperadoCto(0.0);
        posicion.setMontoOperadoTotal(0.0);
        posicion.setMontoCanceladoVcp(0.0);
        posicion.setMontoCanceladoVct(0.0);
        posicion.setMontoCanceladoCto(0.0);
        posicion.setMontoCanceladoTotal(0.0);
        posicion.setMontoModificadoVcp(0.0);
        posicion.setMontoModificadoVct(0.0);
        posicion.setMontoModificadoCto(0.0);
        posicion.setMontoModificadoTotal(0.0);
        posicion.setPosicionVcp(2302.0);
        posicion.setPosicionVct(0.0);
        posicion.setPosicionCto(2302.0);
        posicion.setPosicionTotal(2302.0);
        return posicion;
    }
    
    private Posicion crearPosicionFIBRAM12() {
        Posicion posicion = new Posicion();
        posicion.setEmisor("FIBRAM 12");
        posicion.setSerie("CF");
        posicion.setTv("");
        posicion.setSaldoInicial(10500.0);
        posicion.setSaldoAnteriorVcp(0.0);
        posicion.setSaldoAnteriorVct(10500.0);
        posicion.setSaldoAnteriorCto(0.0);
        posicion.setMontoOperadoVcp(0.0);
        posicion.setMontoOperadoVct(0.0);
        posicion.setMontoOperadoCto(0.0);
        posicion.setMontoOperadoTotal(0.0);
        posicion.setMontoCanceladoVcp(0.0);
        posicion.setMontoCanceladoVct(0.0);
        posicion.setMontoCanceladoCto(0.0);
        posicion.setMontoCanceladoTotal(0.0);
        posicion.setMontoModificadoVcp(0.0);
        posicion.setMontoModificadoVct(0.0);
        posicion.setMontoModificadoCto(0.0);
        posicion.setMontoModificadoTotal(0.0);
        posicion.setPosicionVcp(10500.0);
        posicion.setPosicionVct(0.0);
        posicion.setPosicionCto(10500.0);
        posicion.setPosicionTotal(10500.0);
        return posicion;
    }
    
    private Posicion crearPosicionNAFTRAISHRS() {
        Posicion posicion = new Posicion();
        posicion.setEmisor("NAFTRA ISHRS");
        posicion.setSerie("1B");
        posicion.setTv("");
        posicion.setSaldoInicial(533200.0);
        posicion.setSaldoAnteriorVcp(0.0);
        posicion.setSaldoAnteriorVct(533200.0);
        posicion.setSaldoAnteriorCto(0.0);
        posicion.setMontoOperadoVcp(0.0);
        posicion.setMontoOperadoVct(0.0);
        posicion.setMontoOperadoCto(0.0);
        posicion.setMontoOperadoTotal(0.0);
        posicion.setMontoCanceladoVcp(0.0);
        posicion.setMontoCanceladoVct(0.0);
        posicion.setMontoCanceladoCto(0.0);
        posicion.setMontoCanceladoTotal(0.0);
        posicion.setMontoModificadoVcp(0.0);
        posicion.setMontoModificadoVct(0.0);
        posicion.setMontoModificadoCto(0.0);
        posicion.setMontoModificadoTotal(0.0);
        posicion.setPosicionVcp(533200.0);
        posicion.setPosicionVct(0.0);
        posicion.setPosicionCto(533200.0);
        posicion.setPosicionTotal(533200.0);
        return posicion;
    }
    
    private Posicion crearPosicionSMARTR14() {
        Posicion posicion = new Posicion();
        posicion.setEmisor("SMARTR 14");
        posicion.setSerie("1B");
        posicion.setTv("");
        posicion.setSaldoInicial(15900.0);
        posicion.setSaldoAnteriorVcp(0.0);
        posicion.setSaldoAnteriorVct(15900.0);
        posicion.setSaldoAnteriorCto(0.0);
        posicion.setMontoOperadoVcp(0.0);
        posicion.setMontoOperadoVct(0.0);
        posicion.setMontoOperadoCto(0.0);
        posicion.setMontoOperadoTotal(0.0);
        posicion.setMontoCanceladoVcp(0.0);
        posicion.setMontoCanceladoVct(0.0);
        posicion.setMontoCanceladoCto(0.0);
        posicion.setMontoCanceladoTotal(0.0);
        posicion.setMontoModificadoVcp(0.0);
        posicion.setMontoModificadoVct(0.0);
        posicion.setMontoModificadoCto(0.0);
        posicion.setMontoModificadoTotal(0.0);
        posicion.setPosicionVcp(15900.0);
        posicion.setPosicionVct(0.0);
        posicion.setPosicionCto(15900.0);
        posicion.setPosicionTotal(15900.0);
        return posicion;
    }
    
    private Posicion crearPosicionWALMEX() {
        Posicion posicion = new Posicion();
        posicion.setEmisor("WALMEX");
        posicion.setSerie("1");
        posicion.setTv("");
        posicion.setSaldoInicial(9955.0);
        posicion.setSaldoAnteriorVcp(300.0);
        posicion.setSaldoAnteriorVct(9655.0);
        posicion.setSaldoAnteriorCto(0.0);
        posicion.setMontoOperadoVcp(0.0);
        posicion.setMontoOperadoVct(0.0);
        posicion.setMontoOperadoCto(0.0);
        posicion.setMontoOperadoTotal(0.0);
        posicion.setMontoCanceladoVcp(0.0);
        posicion.setMontoCanceladoVct(0.0);
        posicion.setMontoCanceladoCto(0.0);
        posicion.setMontoCanceladoTotal(0.0);
        posicion.setMontoModificadoVcp(0.0);
        posicion.setMontoModificadoVct(0.0);
        posicion.setMontoModificadoCto(0.0);
        posicion.setMontoModificadoTotal(0.0);
        posicion.setPosicionVcp(300.0);
        posicion.setPosicionVct(9666.0);
        posicion.setPosicionCto(0.0);
        posicion.setPosicionTotal(9966.0);
        return posicion;
    }
}
