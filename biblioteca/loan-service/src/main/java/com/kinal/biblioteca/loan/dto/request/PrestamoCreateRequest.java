package com.kinal.biblioteca.loan.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoCreateRequest {

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;
}