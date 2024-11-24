package com.inventory.app.services;

import java.util.List;
import java.util.Optional;

import com.inventory.app.models.dto.CategoryDto;
import com.inventory.app.models.entities.Category;
import com.inventory.app.models.requests.CategoryRequest;

public interface CategoryService {

    // Lista todas las categorias
    public List<CategoryDto> findAll();

    // Obtiene una categoria por su id
    public Optional<CategoryDto> findById(Long id);

    // Obtiene una categoria por su nombre
    public Optional<CategoryDto> findByName(String name);

    // Obtiene el estado de una categoria por su id
    public Boolean getStatusById(Long id);

    // Guarda una categoria
    public CategoryDto save(Category category);

    // Actualiza una categoria por su id
    public Optional<CategoryDto> update(Long id, CategoryRequest categoryRequest);

    // Elimina una categoria por su id
    void deleteById(Long id);

}
