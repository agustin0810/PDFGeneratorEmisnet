package com.bmv.emisnet.pdfgenerator.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Modelo de datos para aviso de extemporaneidad - SOLO PARA PRUEBAS
 * Este modelo se usa exclusivamente en las pruebas unitarias
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvisoExtemporaneidadTest {
    
    private String fechaGeneracion;
    private String claveCotizacion;
    private String razonSocial;
    private String tipoInformacion;
    private String causasIncumplimiento;
    private String observaciones;
    private LocalDate fechaIncumplimiento;
    private String responsable;
    private String telefono;
    private String email;
}
