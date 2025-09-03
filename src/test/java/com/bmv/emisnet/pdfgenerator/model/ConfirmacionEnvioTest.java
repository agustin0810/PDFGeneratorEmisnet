package com.bmv.emisnet.pdfgenerator.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de datos para confirmación de envío - SOLO PARA PRUEBAS
 * Este modelo se usa exclusivamente en las pruebas unitarias
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmacionEnvioTest {
    
    private String fechaHoraEnvio;
    private String clave;
    private String razonSocial;
    private String folioRecepcion;
    private String responsable;
    private String periodo;
    private List<ArchivoRecibido> archivos;
    private LocalDateTime timestampEnvio;
    private String estado;
    
    /**
     * Clase interna para archivos recibidos
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArchivoRecibido {
        private String nombre;
        private String descripcion;
        private Long tamano;
        private String tipoArchivo;
    }
}
