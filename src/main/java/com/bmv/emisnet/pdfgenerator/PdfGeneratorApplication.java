package com.bmv.emisnet.pdfgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * PDF Generator Application - BMV System
 * 
 * Main Spring Boot application class for the PDF Generator service.
 * This application is designed to integrate with the EMISNET project
 * and provides PDF generation capabilities.
 * 
 * @author 2H Software
 * @organization Bolsa Mexicana de Valores
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class PdfGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfGeneratorApplication.class, args);
    }
}
