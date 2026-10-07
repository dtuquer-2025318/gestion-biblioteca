package com.kinal.biblioteca.loan.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibroDTO {
    private Long id;
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private Integer stockTotal;
    private Integer stockDisponible;
}