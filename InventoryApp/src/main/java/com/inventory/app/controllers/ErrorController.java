package com.inventory.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Este controlador maneja los modelos relacionados a errores 403 y 404 que pueden surgir en la aplicacion web
// La clase hereda la interfaz ErrorController de Java, el cual se utiliza para definir la ruta de error
@Controller
public class ErrorController extends CategoryExtends
        implements org.springframework.boot.web.servlet.error.ErrorController {

    // Error 403, se produce cuando el usuario no tiene los roles asignados para
    // visualizar el recurso protegido
    @GetMapping("/403")
    public String handleAccessDenied403(Model model) {

        // No olvidar generar las categorias
        generateCategories(model);

        return "error403.html";

    }

    // Error 404, comunmente se genera cuando intenta acceder a cualquier endpoint
    // no definido en el servidor
    // Endpoint hacia la pagina de error
    @GetMapping("/error")
    public String getErrorPage(Model model) {

        generateCategories(model);
        return "error404.html";

    }

    // // Método publico para devolver la ruta de la página de error
    public String getErrorPath() {

        return "/error";

    }

}
