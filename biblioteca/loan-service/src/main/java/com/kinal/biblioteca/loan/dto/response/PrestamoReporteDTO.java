package com.kinal.biblioteca.loan.controller.dto.response;

import com.kinal.biblioteca.loan.entity.EstadoPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoReporteDTO {
    private Long prestamoId;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private EstadoPrestamo estado;
    private String usuarioEmail;
    private String usuarioNombre;
    private Long libroId;
    private String libroTitulo;
    private String libroIsbn;
}