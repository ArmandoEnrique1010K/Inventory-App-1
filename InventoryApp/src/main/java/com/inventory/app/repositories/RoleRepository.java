package com.inventory.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.inventory.app.models.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    // Encuentra un rol por su nombre
    Optional<Role> findByName(String name);

}
