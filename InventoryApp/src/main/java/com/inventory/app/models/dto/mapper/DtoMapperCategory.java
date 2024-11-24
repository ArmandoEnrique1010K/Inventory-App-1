package com.inventory.app.models.dto.mapper;

import com.inventory.app.models.dto.CategoryDto;
import com.inventory.app.models.entities.Category;

public class DtoMapperCategory {
    private Category category;

    private DtoMapperCategory() {

    };

    public static DtoMapperCategory builder() {
        return new DtoMapperCategory();
    }

    public DtoMapperCategory setCategory(Category category) {
        this.category = category;
        return this;
    }

    public CategoryDto build() {
        if (category == null) {
            throw new RuntimeException("Debe pasar la entidad categoria");
        }

        return new CategoryDto(this.category.getId(),
                category.getName(),
                category.getColor());
    }
}
