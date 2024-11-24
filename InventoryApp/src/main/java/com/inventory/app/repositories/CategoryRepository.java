package com.inventory.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.inventory.app.models.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    // Encuentra todas las categorias, cuyo campo status sea igual a true
    List<Category> findByStatusTrue();

    // Encuentra una categoria por su nombre
    Optional<Category> findByName(String name);

}
