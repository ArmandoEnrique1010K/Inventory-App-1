package com.inventory.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.inventory.app.models.entities.Product;

// Se definen los métodos para realizar consultas a la base de datos
public interface ProductRepository extends CrudRepository<Product, Long> {

        // Devuelve una página de productos filtrados por estado true, estado de
        // categoria true y ordenados de forma descendente por la fecha de modificación
        @Query("SELECT p FROM Product p JOIN p.category c WHERE p.status = true AND c.status = true ORDER BY p.updateDate DESC")
        Page<Product> pageByStatusTrueAndCategoryStatusTrueOrderByUpdateDateDesc(
                        Pageable pageable);

        // Devuelve una página de productos filtrados por estado true, estado de
        // categoria true, id de categoria y ordenados de forma descendente por la fecha
        // de modificación.
        @Query("SELECT p FROM Product p JOIN p.category c WHERE p.status = true AND c.status = true AND c.id = :categoryId ORDER BY p.updateDate DESC")
        Page<Product> pageByStatusTrueAndCategoryStatusTrueAndCategoryIdOrderByUpdateDateDesc(
                        Pageable pageable, @Param("categoryId") Long categoryId);

        // Devuelve una página de productos filtrados por estado true, estado de
        // categoria true, id de categoria (si no lo tiene, no aplica ese parametro),
        // una palabra clave en el nombre del producto y ordenados de forma descendente
        // por la fecha de modificación.
        @Query("SELECT p FROM Product p JOIN p.category c WHERE p.status = true AND c.status = true AND (:categoryId IS NULL OR c.id = :categoryId) AND p.name LIKE %:p_name% ORDER BY p.updateDate DESC")
        Page<Product> pageByStatusTrueAndCategoryStatusTrueAndCategoryIdAndNameLikeOrderByUpdateDateDesc(
                        Pageable pageable, @Param("categoryId") Long categoryId, @Param("p_name") String p_name);

        // Cuenta la cantidad de productos que coinciden por estado true, estado de
        // categoria true, id de categoria (si no lo tiene, no aplica ese parametro) y
        // una palabra clave en el nombre del producto, ordenados de forma descendente
        // por la fecha de modificación.
        @Query("SELECT COUNT(p) FROM Product p JOIN p.category c WHERE p.status = true AND c.status = true AND (:categoryId IS NULL OR c.id = :categoryId) AND p.name LIKE %:p_name% ORDER BY p.updateDate DESC")
        Long countByStatusTrueAndCategoryStatusTrueAndCategoryIdAndNameLikeOrderByUpdateDateDesc(
                        @Param("categoryId") Long categoryId,
                        @Param("p_name") String p_name);

}