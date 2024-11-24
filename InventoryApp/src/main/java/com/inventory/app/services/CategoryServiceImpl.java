package com.inventory.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.app.models.dto.CategoryDto;
import com.inventory.app.models.dto.mapper.DtoMapperCategory;
import com.inventory.app.models.entities.Category;
import com.inventory.app.models.requests.CategoryRequest;
import com.inventory.app.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lista todas las categorias
    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> findAll() {

        List<Category> categories = categoryRepository.findByStatusTrue();
        return categories.stream().map(c -> DtoMapperCategory.builder().setCategory(c).build())
                .collect(Collectors.toList());

    }

    // Obtiene una categoria por su id
    @Transactional(readOnly = true)
    @Override
    public Optional<CategoryDto> findById(Long id) {

        return categoryRepository.findById(id)
                .map(category -> DtoMapperCategory.builder().setCategory(category).build());

    }

    // Obtiene una categoria por su nombre
    @Transactional(readOnly = true)
    @Override
    public Optional<CategoryDto> findByName(String name) {

        return categoryRepository.findByName(name)
                .map(category -> DtoMapperCategory.builder().setCategory(category).build());

    }

    // Obtiene el estado de una categoria por su id
    @Transactional(readOnly = true)
    @Override
    public Boolean getStatusById(Long id) {

        Category getCategory = categoryRepository.findById(id).orElseThrow();
        return getCategory.getStatus();

    }

    // Guarda una categoria
    @Transactional
    @Override
    public CategoryDto save(Category category) {

        category.setStatus(true);
        return DtoMapperCategory.builder().setCategory(categoryRepository.save(category)).build();

    }

    // Actualiza una categoria por su id
    @Transactional
    @Override
    public Optional<CategoryDto> update(Long id, CategoryRequest categoryRequest) {

        Optional<Category> categoryById = categoryRepository.findById(id);

        Category categoryOptional = null;

        // Si la categoria existe, lo actualiza con los datos proporcionados
        if (categoryById.isPresent()) {
            Category categoryData = categoryById.orElseThrow();

            categoryData.setName(categoryRequest.getName());
            categoryData.setColor(categoryRequest.getColor());

            categoryOptional = categoryRepository.save(categoryData);
        }

        return Optional.ofNullable(DtoMapperCategory.builder().setCategory(categoryOptional).build());

    }

    // Elimina una categoria por su id
    @Transactional
    @Override
    public void deleteById(Long id) {

        Category deletedCategory = categoryRepository.findById(id).orElseThrow();

        // Cambia el estado de la categoria a false y lo guarda
        deletedCategory.setStatus(false);
        categoryRepository.save(deletedCategory);

    }

}
