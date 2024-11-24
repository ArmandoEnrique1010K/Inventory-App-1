package com.inventory.app.models.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

// En esta clase DTO se definen solamente los campos necesarios que se van a
// utilizar para mostrar la información, se puede ocultar información sensible
// como el estado o la fecha de modificación
public class ProductDto {

    private Long id;
    private String name;

    // No olvidar aplicar el formato de fecha
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private LocalDate entryDate;
    private Double length;
    private Double width;
    private Integer quantity;
    private String nameImage;
    private MultipartFile fileImage;

    // Campos para asignar la categoria
    private String categoryName;
    private String categoryColor;

}

// Nota: En el caso de que haya campos que se tenga que validar y se quieren
// omitir esos campos que se utilizaron para "editar" un registro en la base de
// datos, se tendria que crear la clase ProductRequest