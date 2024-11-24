package com.inventory.app.models.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categorias")
public class Category {

    // Identificador unico
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre
    @NotBlank(message = "Introduzca un nombre")
    private String name;

    // Codigo del color asignado (formato RGB o Hexadecimal)
    // Por defecto, en un <input> de tipo color se asigna el color negro (#000000)
    @NotBlank
    private String color;

    // Estado: true o false
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean status;

    // Relacion hacia la entidad Product
    // OneToMany especifica una relación de unos a muchos
    // El campo es de tipo List, cada elemento debe ser de tipo Product
    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
