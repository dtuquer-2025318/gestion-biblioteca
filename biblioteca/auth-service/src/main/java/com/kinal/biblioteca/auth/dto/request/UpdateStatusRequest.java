package com.kinal.biblioteca.auth.dto.request;

import com.kinal.biblioteca.auth.entity.EstadoUsuario;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    @NotNull(message = "El estado es obligatorio")
    private EstadoUsuario estado;
}