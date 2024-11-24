package com.inventory.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inventory.app.models.entities.Category;
import com.inventory.app.models.requests.CategoryRequest;
import com.inventory.app.services.CategoryService;

import jakarta.validation.Valid;

// RequestMapping sirve para definir un endpoint que se instanciara en los demás
// endpoints
// El controlador hereda la clase CategoryExtends
@Controller
@RequestMapping("/categories")
public class CategoryController extends CategoryExtends {

    // Inyecta una instancia de categoryService
    @Autowired
    private CategoryService categoryService;

    // Ruta hacia la lista de categorias
    // Model representa el modelo para la vista
    @GetMapping
    public String listAllCategories(Model model) {

        // Llama al método definido en CategoryExtends para generar la lista de
        // categorías.
        generateCategories(model);

        // Devuelve la página listCategories
        return "listCategories";

    }

    // Ruta hacia el formulario de agregar categoria
    @GetMapping("/add")
    public String formAddCategory(Model model) {

        // Crea un nuevo objeto de tipo Category
        Category category = new Category();

        // Añade el atributo category al modelo
        model.addAttribute("category", category);

        generateCategories(model);
        return "formAddCategory";

    }

    // Metodo para agregar la categoria desde el formulario
    // category representa la entidad Category validada
    // result es el resultado de la validación
    @PostMapping("/add")
    public String saveCategory(@ModelAttribute("category") @Valid Category category,
            BindingResult result,
            Model model) {

        // Si hay algun error de validación, mantiene los campos que fueron rellenados y
        // renderiza la pagina formAddCategory
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            generateCategories(model);
            return "formAddCategory.html";
        }

        // Llama al servicio para guardar la categoria
        categoryService.save(category);

        return "redirect:/categories";

    }

    // Ruta hacia el formulario de editar categoria
    // id representa el id de la categoria
    // PathVariable sirve para manejar una endpoint dinamico
    @GetMapping("/edit/{id}")
    public String formEditCategory(@PathVariable Long id, Model model) {

        // Almacena el estado de la categoria por su id
        var categoryStatus = categoryService.getStatusById(id);

        // Si la categoria tiene el status false, renderiza la pagina de error204 y
        // tambien se generan las categorias para la barra de menu
        if (categoryStatus == false) {

            generateCategories(model);
            return "error204";

        } else {
            // De lo contrario, almacena la categoria obtenida por su id
            var categoryData = categoryService.findById(id).orElseThrow();

            // El atributo sirve para obtener los datos de la categoria y mostrarlos en el
            // modelo
            model.addAttribute("category", categoryData);
            // Añade solamente el id al modelo
            model.addAttribute("id", categoryData.getId());
            generateCategories(model);
            return "formEditCategory.html";

        }

    }

    // Metodo para editar la categoria desde el formulario
    // categoryRequest representa el request con los datos de la categoria (se
    // omiten ciertos campos)
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id,
            @ModelAttribute("category") @Valid CategoryRequest categoryRequest,
            BindingResult result,
            Model model) {

        // Verifica si hay errores de validación y si los hay renderiza el formulario
        // para editar con los datos introducidos hasta el momento
        if (result.hasErrors()) {
            // Obtiene la categoria por el id
            var categoryData = categoryService.findById(id).orElseThrow();
            // Recupera los valores introducidos en el formulario
            model.addAttribute("category", categoryRequest);
            model.addAttribute("id", categoryData.getId());
            generateCategories(model);
            return "formEditCategory.html";
        }

        // Recuperar el ProductDto existente (No sirve porque no se elimina la categoria
        // de la base de datos)
        // categoryService.findById(id).orElseThrow(() -> new
        // RuntimeException("Categoria no encontrada"));

        // Llama al servicio para actualizar la categoria, requiere el id y los datos
        // introducidos
        categoryService.update(id, categoryRequest);

        // Redirecciona al endpoint /categories
        return "redirect:/categories";

    }

    // Metodo para eliminar la categoria
    // Las categorias eliminadas no se muestran en el modelo
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);

        // Si el id es mayor que 0 (redundante)
        // if (id > 0) {
        // categoryService.deleteById(id);
        // }

        return "redirect:/categories";

    }

}
