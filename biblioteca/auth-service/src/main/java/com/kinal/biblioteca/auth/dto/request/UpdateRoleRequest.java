package com.kinal.biblioteca.auth.dto.request;

import com.kinal.biblioteca.auth.entity.Rol;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}