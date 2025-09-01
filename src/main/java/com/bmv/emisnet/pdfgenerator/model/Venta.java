package com.bmv.emisnet.pdfgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Modelo de datos para una venta individual
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private LocalDate fecha;
    private String cliente;
    private String producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
}
