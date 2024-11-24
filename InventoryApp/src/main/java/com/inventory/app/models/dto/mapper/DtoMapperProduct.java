package com.inventory.app.models.dto.mapper;

import com.inventory.app.models.dto.ProductDto;
import com.inventory.app.models.entities.Product;

// Clase para mapear la entidad Product a su correspondiente DTO(Data Transfer Object).
public class DtoMapperProduct {

    // Instancia de Producto que se va a mapear
    private Product product;

    // Constructor privado para evitar la instanciación directa.
    private DtoMapperProduct() {
    };

    // Método estático para crear una nueva instancia de DtoMapperProducto
    public static DtoMapperProduct builder() {
        return new DtoMapperProduct();
    }

    // Método para establecer la entidad Product a mapear
    public DtoMapperProduct setProduct(Product product) {
        // Asigna el product
        this.product = product;
        // Devuelve la instancia actual para permitir el encadenamiento de métodos
        return this;
    }

    // Método para construir el ProductDto a partir de la entidad Product
    // establecido
    public ProductDto build() {

        // Verifica si se ha establecido un producto
        if (product == null) {
            throw new RuntimeException("Debe pasar la entidad producto");
        }

        // Crea y devuelve un nuevo ProductDto basado en la entidad Product y su
        // relación con la entidad Category
        return new ProductDto(this.product.getId(),
                product.getName(),
                product.getEntryDate(),
                product.getLength(),
                product.getWidth(),
                product.getQuantity(),
                product.getNameImage(),
                product.getFileImage(),

                // Campos para la categoria
                product.getCategory().getName(),
                product.getCategory().getColor());

    }

}

// Se repite el mismo procedimiento con DtoMapperCategory y DtoMapperUser.
