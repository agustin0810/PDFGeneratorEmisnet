package com.bmv.emisnet.pdfgenerator.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Modelo de datos para reporte de posiciones - SOLO PARA PRUEBAS
 * Este modelo se usa exclusivamente en las pruebas unitarias
 * Estructura que coincide con la plantilla reporte-posiciones.html
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportePosicionesTest {
    
    private String fechaOperacion;
    private String casaBolsa;
    private String razonSocial;
    private List<GrupoPosiciones> gruposPosiciones;
    
    /**
     * Clase interna para grupos de posiciones
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GrupoPosiciones {
        private String nombre;
        private List<PosicionDetalle> posiciones;
    }
    
    /**
     * Clase interna para detalles de posiciones
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PosicionDetalle {
        // EMISIÓN
        private String emisora;
        private String serie;
        private String tv;
        
        // SALDO ANTERIOR
        private Integer saldoInicial;
        private Integer saldoAnteriorVcp;
        private Integer saldoAnteriorVct;
        private Integer saldoAnteriorCto;
        
        // MONTO OPERADO
        private Integer montoOperadoVcp;
        private Integer montoOperadoVct;
        private Integer montoOperadoCto;
        private Integer montoOperadoTotal;
        
        // MONTO CANCELADO
        private Integer montoCanceladoVcp;
        private Integer montoCanceladoVct;
        private Integer montoCanceladoCto;
        private Integer montoCanceladoTotal;
        
        // MONTO MODIFICADO
        private Integer montoModificadoVcp;
        private Integer montoModificadoVct;
        private Integer montoModificadoCto;
        private Integer montoModificadoTotal;
        
        // POSICIÓN
        private Integer posicionVcp;
        private Integer posicionVct;
        private Integer posicionCto;
        private Integer posicionTotal;
    }
}
