package com.inventory.app.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

// Utiliza 2 nuevos campos para establecer una nueva contraseña
public class UserPasswordRequest {

    @NotBlank(message = "Introduzca su contraseña anterior")
    private String currentPassword;

    @NotBlank(message = "Introduzca su nueva contraseña")
    private String newPassword;

}
