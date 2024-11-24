package com.inventory.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.inventory.app.models.dto.CategoryDto;
import com.inventory.app.services.CategoryService;

// Esta clase no es un controlador, es una clase que contiene codigo que se instancia en distintos controladores
public class CategoryExtends {

    // Instancia del servicio
    @Autowired
    private CategoryService categoryService;

    // Metodo vacio para obtener la lista de categorias (requiere de un parametro
    // model)
    // Logicamente en la aplicacion web hay un menu de navegación, en el cual se
    // genera la lista de categorias y para evitar tener que repetir el mismo codigo
    // varias veces, se utiliza el termino extends en las clases que van a heredar
    // esta clase
    public void generateCategories(Model model) {

        // Obtiene la lista de categorias
        List<CategoryDto> categories = categoryService.findAll();

        // Añade el atributo categories en una pagina web
        model.addAttribute("categories", categories);

    }

}
