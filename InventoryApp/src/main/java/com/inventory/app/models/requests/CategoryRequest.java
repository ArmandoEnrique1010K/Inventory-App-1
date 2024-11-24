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
public class CategoryRequest {

    // private Long id;

    @NotBlank(message = "Introduzca un nombre")
    private String name;

    @NotBlank
    private String color;

}
