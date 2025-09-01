package com.bmv.emisnet.pdfgenerator.controller;

import com.bmv.emisnet.pdfgenerator.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PDF Controller
 * 
 * REST controller for handling PDF generation requests.
 * Provides endpoints for different types of PDF generation.
 */
@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
@Slf4j
public class PdfController {

    private final PdfGeneratorService pdfGeneratorService;

    /**
     * Generate PDF from HTML content
     * 
     * @param htmlContent HTML content to convert
     * @return Generated PDF as byte array
     */
    @PostMapping("/generate/html")
    public ResponseEntity<byte[]> generateFromHtml(@RequestBody String htmlContent) {
        log.info("Received request to generate PDF from HTML");
        
        byte[] pdfContent = pdfGeneratorService.generatePdfFromHtml(htmlContent);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "generated.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }

    /**
     * Generate PDF from template
     * 
     * @param templateName Template name
     * @param data Template data
     * @return Generated PDF as byte array
     */
    @PostMapping("/generate/template/{templateName}")
    public ResponseEntity<byte[]> generateFromTemplate(
            @PathVariable String templateName,
            @RequestBody Object data) {
        
        log.info("Received request to generate PDF from template: {}", templateName);
        
        byte[] pdfContent = pdfGeneratorService.generatePdfFromTemplate(templateName, data);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", templateName + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }

    /**
     * Health check endpoint
     * 
     * @return Simple health check response
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PDF Generator Service is running");
    }
}
