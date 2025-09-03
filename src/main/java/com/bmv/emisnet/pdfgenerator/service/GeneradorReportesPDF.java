package com.bmv.emisnet.pdfgenerator.service;

import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Servicio genérico para generar PDFs usando plantillas HTML con Thymeleaf
 * y conversión a PDF con OpenPDF/Flying Saucer
 * 
 * Esta clase está diseñada para ser utilizada como librería reutilizable
 * que puede generar PDFs a partir de cualquier plantilla Thymeleaf y datos.
 * La orientación del PDF se define en la plantilla HTML con CSS.
 * Todos los métodos retornan byte[] para máxima flexibilidad de uso.
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
     * Genera un PDF a partir de una plantilla y datos específicos
     * La orientación se define en la plantilla HTML con CSS (@page { size: A4 landscape; })
     * 
     * @param nombrePlantilla Nombre de la plantilla (sin extensión .html)
     * @param datos Datos a inyectar en la plantilla
     * @return Array de bytes con el contenido del PDF
     * @throws IOException Si hay error de I/O
     * @throws DocumentException Si hay error en la generación del PDF
     */
    public byte[] generarPDF(String nombrePlantilla, Map<String, Object> datos) 
            throws IOException, DocumentException {
        
        String htmlContent = procesarPlantilla(nombrePlantilla, datos);
        
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            
            // Configurar documento HTML
            renderer.setDocumentFromString(htmlContent);
            
            // Configurar la ruta base para recursos (imágenes, CSS, etc.)
            // Esto permite que el renderer encuentre las imágenes con rutas relativas
            String baseUrl = this.getClass().getClassLoader().getResource("templates/").toString();
            renderer.getSharedContext().setBaseURL(baseUrl);
            
            renderer.layout();
            renderer.createPDF(outputStream);
            
            log.debug("PDF generado en memoria exitosamente");
            return outputStream.toByteArray();
        }
    }
    
    /**
     * Procesa una plantilla Thymeleaf con los datos proporcionados
     * 
     * @param nombrePlantilla Nombre de la plantilla (sin extensión .html)
     * @param datos Datos a inyectar en la plantilla
     * @return Contenido HTML procesado
     */
    public String procesarPlantilla(String nombrePlantilla, Map<String, Object> datos) {
        Context context = new Context();
        
        // Agregar todos los datos al contexto de Thymeleaf
        if (datos != null) {
            datos.forEach(context::setVariable);
        }
        
        return templateEngine.process(nombrePlantilla, context);
    }
    
    /**
     * Genera un PDF a partir de una plantilla y un objeto Java
     * Convierte automáticamente el objeto a un Map usando ObjectToMap
     * 
     * @param nombrePlantilla Nombre de la plantilla (sin extensión .html)
     * @param objeto Objeto a convertir y usar como datos
     * @return Array de bytes con el contenido del PDF
     * @throws IOException Si hay error de I/O
     * @throws DocumentException Si hay error en la generación del PDF
     */
    public byte[] generarPDFDesdeObjeto(String nombrePlantilla, Object objeto) 
            throws IOException, DocumentException {
        
        Map<String, Object> datos = ObjectToMapConverter.convertToMap(objeto);
        return generarPDF(nombrePlantilla, datos);
    }
    
    /**
     * Genera un PDF a partir de una plantilla y un objeto Java incluyendo herencia
     * Convierte automáticamente el objeto a un Map incluyendo campos de superclases
     * 
     * @param nombrePlantilla Nombre de la plantilla (sin extensión .html)
     * @param objeto Objeto a convertir y usar como datos
     * @return Array de bytes con el contenido del PDF
     * @throws IOException Si hay error de I/O
     * @throws DocumentException Si hay error en la generación del PDF
     */
    public byte[] generarPDFDesdeObjetoConHerencia(String nombrePlantilla, Object objeto) 
            throws IOException, DocumentException {
        
        Map<String, Object> datos = ObjectToMapConverter.convertToMapIncludingInheritance(objeto);
        return generarPDF(nombrePlantilla, datos);
    }
    
    /**
     * Genera un PDF a partir de una plantilla y un objeto Java excluyendo campos específicos
     * 
     * @param nombrePlantilla Nombre de la plantilla (sin extensión .html)
     * @param objeto Objeto a convertir y usar como datos
     * @param camposExcluir Campos que no se incluirán en el mapa
     * @return Array de bytes con el contenido del PDF
     * @throws IOException Si hay error de I/O
     * @throws DocumentException Si hay error en la generación del PDF
     */
    public byte[] generarPDFDesdeObjetoExcluyendo(String nombrePlantilla, Object objeto, String... camposExcluir) 
            throws IOException, DocumentException {
        
        Map<String, Object> datos = ObjectToMapConverter.convertToMapExcluding(objeto, camposExcluir);
        return generarPDF(nombrePlantilla, datos);
    }
}
