package com.bmv.emisnet.pdfgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Modelo que contiene todos los datos necesarios para generar un reporte
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosReporte {
    private String periodo;
    private List<Venta> ventas;
}
