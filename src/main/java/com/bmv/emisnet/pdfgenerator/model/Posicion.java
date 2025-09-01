package com.bmv.emisnet.pdfgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo para representar una posición en el reporte de consulta de posiciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posicion {
    
    private String emisor;
    private String serie;
    private String tv;
    
    // Saldo Anterior
    private double saldoInicial;
    private double saldoAnteriorVcp;
    private double saldoAnteriorVct;
    private double saldoAnteriorCto;
    
    // Monto Operado
    private double montoOperadoVcp;
    private double montoOperadoVct;
    private double montoOperadoCto;
    private double montoOperadoTotal;
    
    // Monto Cancelado
    private double montoCanceladoVcp;
    private double montoCanceladoVct;
    private double montoCanceladoCto;
    private double montoCanceladoTotal;
    
    // Monto Modificado
    private double montoModificadoVcp;
    private double montoModificadoVct;
    private double montoModificadoCto;
    private double montoModificadoTotal;
    
    // Posición
    private double posicionVcp;
    private double posicionVct;
    private double posicionCto;
    private double posicionTotal;
}
