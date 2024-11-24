package com.inventory.app.services;

import java.util.List;
import java.util.Optional;

import com.inventory.app.models.dto.UserDto;
import com.inventory.app.models.entities.User;
import com.inventory.app.models.requests.UserDataRequest;
import com.inventory.app.models.requests.UserPasswordRequest;

public interface UserService {

    // Lista todos los usuarios
    List<UserDto> findAllUsers();

    // Obtiene el usuario por su id
    Optional<UserDto> findById(Long id);

    // Obtiene el usuario por su email
    Optional<UserDto> findByEmail(String email);

    // Verifica si existe el usuario registrado
    Boolean getUserByEmail(String email);

    // Guarda el usuario
    UserDto save(User user);

    // Actualiza los datos del usuario
    Optional<UserDto> update(UserDataRequest userDataRequest, String email);

    // Actualiza la contraseña del usuario
    Boolean validateAndUpdatePassword(UserPasswordRequest userPasswordRequest, String email);

    // Elimina definitivamente el usuario por su email
    // (no se recupera una vez eliminado)
    void remove(Long id);

}
