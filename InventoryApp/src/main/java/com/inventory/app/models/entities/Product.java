package com.inventory.app.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

// Define esta clase como una entidad
@Entity

// Anotaciones importadas por lombok
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

// Nombre de la tabla en la base de datos
@Table(name = "productos")
public class Product {

    // Campo para asignar un identificador unico (id), es autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo para el nombre, no puede estar en blanco
    @NotBlank(message = "Introduzca un nombre")
    private String name;

    // La anotación @DateTimeFormat aplica un formato de fecha
    // La fecha de ingreso del producto
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;

    // La fecha de modificación
    // LocalDateTime define la fecha y hora, incluyendo milisegundos
    private LocalDateTime updateDate;

    // Medidas (largo y ancho) en decimales
    private Double length;
    private Double width;

    // La cantidad (numero entero)
    // Como minimo debe tener el valor 1
    @NotNull(message = "Introduzca una cantidad")
    @Min(value = 1)
    private Integer quantity;

    // El estado tiene 2 valores: true o false
    // Column define algunas propiedades que se aplicaran desde la base de datos
    // columnDefinition sirve para especificar el tipo de dato
    // TINYINT(1) es el campo ideal para evitar el siguiente error en MySQL
    // Error 1406: Data too long for column
    // Sucede al querer importar registros desde un archivo en formato csv
    // nullable en false no permite que el campo tenga el valor null
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean status;

    // El nombre de la imagen
    private String nameImage;

    // El archivo que representa la imagen
    // Transient evita que el campo fileImage forme parte de la entidad
    // MultipartFile representa un archivo recibido
    @Transient
    private MultipartFile fileImage;

    // Relación hacia la entidad Category
    // ManyToOne especifica una relación de muchos a unos
    // JoinColumn especifica el mapeo de una columna para una referencia
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
