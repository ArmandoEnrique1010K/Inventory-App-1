package com.inventory.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controller sirve para definir una clase de tipo controlador
// Se definen las rutas o endpoints de la aplicación
@Controller
public class LoginController {

    // Ruta hacia la pagina de inicio, contiene el login
    // GetMapping generalmente contiene un endpoint, el cual devuelve una pagina web
    // desde el navegador
    // http://localhost:8080/login o http://localhost:8080/
    // RequestParam sirve para definir parametros en el endpoint
    // Model sirve para añadir contenido dinamico desde el controlador a la pagina
    // web como los datos obtenidos desde el servicios
    @GetMapping({ "/", "/login" })
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        // Si el parametro error esta presente en el endpoint
        // http://localhost:8080/login?error=true
        if (error != null) {
            // Muestra un mensaje en la pagina web
            model.addAttribute("error", "Usuario o contraseña incorrectos, inténtalo de nuevo.");
        }

        // Lo mismo ocurre con el parametro logout
        // http://localhost:8080/login?logout=true
        if (logout != null) {
            model.addAttribute("logout", "Has cerrado sesión correctamente.");
        }

        // Devuelve o muestra la pagina de inicio, lleva el nombre de "index"
        return "index";

    }

}
