package com.kinal.biblioteca.loan.dto.response;

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
public class PrestamoResponse {
    private Long id;
    private String usuarioEmail;
    private Long libroId;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private EstadoPrestamo estado;
}