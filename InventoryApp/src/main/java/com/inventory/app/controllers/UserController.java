package com.inventory.app.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inventory.app.models.dto.UserDto;
import com.inventory.app.services.UserService;

// Controlador para manejar las rutas relacionadas a /users
@Controller
@RequestMapping("/users")
public class UserController extends CategoryExtends {

    // Inyecta la dependencia userService
    @Autowired
    private UserService userService;

    // Ruta hacia la pagina que contiene la lista de usuarios registrados en el
    // sistema
    @GetMapping
    public String listUsers(Model model) {

        // Obtiene la lista de usuarios y lo añade en el atributo users
        model.addAttribute("users", userService.findAllUsers());

        // Obtiene la lista de categorias
        generateCategories(model);

        return "listUsers.html";
    }

    // Metodo para borrar definitivamente al usuario del sistema
    // (una vez eliminado no se puede recuperar)
    @PostMapping("/delete/{id}")
    public String removeUser(@PathVariable Long id) {
        Optional<UserDto> userById = userService.findById(id);

        // Si el usuario esta presente, lo elimina y redirecciona al endpoint /users, la
        // lista de usuarios
        if (userById.isPresent()) {
            userService.remove(id);
            return "redirect:/users";
        }

        return "redirect:/users";

    }

}
