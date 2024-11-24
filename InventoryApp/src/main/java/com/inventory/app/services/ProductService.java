package com.inventory.app.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inventory.app.models.dto.ProductDto;
import com.inventory.app.models.entities.Product;
import com.inventory.app.models.requests.ProductRequest;

// Aqui se definen los métodos para realizar operaciones con los datos obtenidos desde el repositorio
public interface ProductService {

    // Lista una pagina con todos productos
    public Page<ProductDto> findAll(Pageable pageable);

    // Lista una pagina con todos productos por el id de su categoria
    public Page<ProductDto> findAllByCategoryId(Pageable pageable, Long categoryId);

    // Lista una pagina de productos por su id de categoria y nombre de producto
    public Page<ProductDto> findAllByCategoryIdAndNameLike(Pageable pageable, Long categoryId, String name);

    // Cuenta la cantidad de productos por su id de categoria y nombre de producto
    public Long countByCategoryIdAndNameLike(Long categoryId, String name);

    // Obtiene un producto su id
    public Optional<ProductDto> findById(Long id);

    // Obtiene el estado de un producto por su id
    public Boolean getStatusById(Long id);

    // Guarda un producto
    public ProductDto save(Product product);

    // Actualiza un producto
    public Optional<ProductDto> update(Long id, ProductRequest productRequest);

    // Elimina un producto por su id (cambia el estado a false)
    void deleteById(Long id);

}