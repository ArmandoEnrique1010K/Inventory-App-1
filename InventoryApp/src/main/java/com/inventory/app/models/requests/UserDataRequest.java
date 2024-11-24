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

// Utiliza los campos necesarios para editar el perfil del usuario
public class UserDataRequest {

    @NotBlank(message = "Introduzca un nombre")
    private String firstname;

    @NotBlank(message = "Introduzca un apellido")
    private String lastname;

}
