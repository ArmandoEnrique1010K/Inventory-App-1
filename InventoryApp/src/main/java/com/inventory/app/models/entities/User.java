package com.inventory.app.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El correo, no puede estar en blanco ni contener solamente espacios en blanco
    // Email valida que tenga el formato correcto
    // unique asegura de que el correo sea unico en la base de datos
    @NotBlank(message = "Introduzca un correo")
    @Email(message = "No se reconoce el correo ingresado")
    @Column(unique = true)
    private String email;

    // Contraseña
    @NotBlank(message = "Introduzca una contraseña")
    private String password;

    // Primer y/o segundo nombre
    @NotBlank(message = "Introduzca un nombre")
    private String firstname;

    // Apellidos
    @NotBlank(message = "Introduzca un apellido")
    private String lastname;

    // Relación hacia la entidad Role
    // ManyToMany define una relación de muchos a muchos.
    // JoinTable especifica la tabla de unión.
    // name es el nombre de la tabla de unión.
    // joinColumns especifica la columna de unión de la entidad User.
    // inverseJoinColumns especifica la columna de unión de la otra entidad (Role).
    // uniqueConstraints asegura que la combinación de user_id y role_id sea única.
    @ManyToMany
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false), uniqueConstraints = {
            @UniqueConstraint(columnNames = { "user_id", "role_id" }) })
    private List<Role> roles;

    // Los siguientes 2 campos no forman parte de la entidad
    // Sirven para asignar el tipo de rol al usuario
    @Transient
    private boolean manager;
    @Transient
    private boolean admin;

}
