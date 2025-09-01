package com.bmv.emisnet.pdfgenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * PDF Generation Configuration Properties
 * 
 * Configuration properties for PDF generation settings.
 */
@Data
@Component
@ConfigurationProperties(prefix = "pdf.generator")
public class PdfProperties {
    
    private String defaultFont = "Arial";
    private int defaultFontSize = 12;
    private String pageSize = "A4";
    private Margins margins = new Margins();
    
    @Data
    public static class Margins {
        private int top = 20;
        private int bottom = 20;
        private int left = 20;
        private int right = 20;
    }
}
