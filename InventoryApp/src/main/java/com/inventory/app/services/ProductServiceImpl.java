package com.inventory.app.services;

import com.inventory.app.models.dto.ProductDto;
import com.inventory.app.models.dto.mapper.DtoMapperProduct;
import com.inventory.app.models.entities.Product;
import com.inventory.app.models.requests.ProductRequest;
import com.inventory.app.repositories.CategoryRepository;
import com.inventory.app.repositories.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    // Autowired sirve para inyectar las demás instancias (clases) que se almacenan
    // en el contexto de Spring
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryRepository categoryRepository;

    // Lista una pagina con todos productos
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Pageable pageable) {

        // Obtiene la lista de productos desde el repositorio
        Page<Product> pageProduct = productRepository
                .pageByStatusTrueAndCategoryStatusTrueOrderByUpdateDateDesc(pageable);

        // Convierte la lista de entidades Product en una lista de ProductDto
        List<ProductDto> productDtoList = pageProduct.stream()
                .map(product -> DtoMapperProduct.builder().setProduct(product).build())
                .collect(Collectors.toList());

        // Retorna la página de ProductDto
        return new PageImpl<>(productDtoList, pageable, pageProduct.getTotalElements());

    }

    // Lista una pagina con todos productos por el id de su categoria
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findAllByCategoryId(Pageable pageable, Long categoryId) {

        Page<Product> pageProduct = (Page<Product>) productRepository
                .pageByStatusTrueAndCategoryStatusTrueAndCategoryIdOrderByUpdateDateDesc(pageable,
                        categoryId);

        List<ProductDto> productDtoList = pageProduct.stream()
                .map(product -> DtoMapperProduct.builder().setProduct(product).build())
                .collect(Collectors.toList());

        return new PageImpl<>(productDtoList, pageable, pageProduct.getTotalElements());

    }

    // Lista una pagina de productos por su id de categoria y nombre de producto
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findAllByCategoryIdAndNameLike(Pageable pageable, Long categoryId, String name) {

        // Obtiene la lista de productos desde el repositorio
        Page<Product> pageProduct = (Page<Product>) productRepository
                .pageByStatusTrueAndCategoryStatusTrueAndCategoryIdAndNameLikeOrderByUpdateDateDesc(
                        pageable, categoryId, name);

        List<ProductDto> productDtoList = pageProduct.stream()
                .map(product -> DtoMapperProduct.builder().setProduct(product).build())
                .collect(Collectors.toList());

        return new PageImpl<>(productDtoList, pageable, pageProduct.getTotalElements());

    }

    // Cuenta la cantidad de productos por su id de categoria y nombre de producto
    @Override
    @Transactional(readOnly = true)
    public Long countByCategoryIdAndNameLike(Long categoryId, String name) {

        return productRepository
                .countByStatusTrueAndCategoryStatusTrueAndCategoryIdAndNameLikeOrderByUpdateDateDesc(
                        categoryId, name);

    }

    // Obtiene un producto su id
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> findById(Long id) {

        // Busca un producto por su id y lo convierte a ProductDto
        return productRepository.findById(id)
                .map(product -> DtoMapperProduct.builder().setProduct(product).build());

    }

    // Obtiene el estado de un producto por su id
    @Override
    @Transactional(readOnly = true)
    public Boolean getStatusById(Long id) {

        // Busca la entidad del producto por su id y devuelve su estado
        Product getProduct = productRepository.findById(id).orElseThrow();
        return getProduct.getStatus();

    }

    // Guarda un producto
    @Override
    @Transactional
    public ProductDto save(Product product) {

        // Establece la fecha de modificación con la fecha y hora actual
        product.setUpdateDate(LocalDateTime.now());

        // Almacena la imagen y obtiene su nombre
        String nameImage = imageService.storeImage(product.getFileImage());
        product.setNameImage(nameImage);

        // Establece el estado del producto a "true" (habilitado o activo)
        product.setStatus(true);

        // Guarda el producto en la base de datos y lo convierte a ProductDto
        return DtoMapperProduct.builder().setProduct(productRepository.save(product)).build();

    }

    // Actualiza un producto
    @Override
    @Transactional
    public Optional<ProductDto> update(Long id, ProductRequest productRequest) {

        // Busca el producto por su id
        Optional<Product> productById = productRepository.findById(id);

        // Define un objeto de tipo Product
        Product product = null;

        // Si el producto existe, lo actualiza con los datos proporcionados
        if (productById.isPresent()) {
            Product productData = productById.orElseThrow();

            // Actualiza los campos del producto
            productData.setName(productRequest.getName());
            productData.setEntryDate(productRequest.getEntryDate());
            productData.setQuantity(productRequest.getQuantity());
            productData.setLength(productRequest.getLength());
            productData.setWidth(productRequest.getWidth());

            // Actualiza la fecha de modificación
            productData.setUpdateDate(LocalDateTime.now());

            // Establece la categoria del producto buscando una categoria por su nombre
            productData.setCategory(
                    categoryRepository.findByName(productRequest.getCategoryName()).orElseThrow());

            // Si hay una imagen nueva, la almacena y actualiza el nombre de la imagen,
            // de lo contrario mantiene la imagen existente
            if (productRequest.getFileImage() != null && !productRequest.getFileImage().isEmpty()) {
                String nameImage = imageService.storeImage(productRequest.getFileImage());
                productData.setNameImage(nameImage);
            } else {
                productData.setFileImage(productRequest.getFileImage());
            }

            // Guarda el producto actualizado en la base de datos
            product = productRepository.save(productData);
        }

        // Retorna el producto actualizado convertido a ProductDto
        return Optional.ofNullable(DtoMapperProduct.builder().setProduct(product).build());

    }

    // Elimina un producto por su id
    @Override
    @Transactional
    public void deleteById(Long id) {

        // Busca el producto por su id y cambia su estado a false
        Product deletedProduct = productRepository.findById(id).orElseThrow();
        deletedProduct.setStatus(false);

        // Guarda el producto editado
        productRepository.save(deletedProduct);

    }

}
