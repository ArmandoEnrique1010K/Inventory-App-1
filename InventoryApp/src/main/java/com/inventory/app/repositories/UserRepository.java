package com.inventory.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.inventory.app.models.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    // Encuentra un usuario por su correo
    Optional<User> findByEmail(String email);

}
