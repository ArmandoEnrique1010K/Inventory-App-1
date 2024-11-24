package com.inventory.app.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;

    // Campos para asignar el rol
    private boolean isManager;
    private boolean isAdmin;

}
