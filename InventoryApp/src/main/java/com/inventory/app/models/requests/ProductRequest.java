package com.inventory.app.models.requests;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

// Esta clase es una variación de la entidad Product, se omiten ciertos campos
public class ProductRequest {

    @NotBlank(message = "Introduzca un nombre")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;

    private Double length;

    private Double width;

    @NotNull(message = "Introduzca una cantidad")
    @Min(value = 1)
    private Integer quantity;

    private String nameImage;

    @Transient
    private MultipartFile fileImage;

    // Utiliza el nombre de la categoria
    private String categoryName;

}
