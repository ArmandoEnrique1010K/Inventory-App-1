package com.inventory.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inventory.app.models.entities.User;
import com.inventory.app.services.UserService;

import jakarta.validation.Valid;

// Controlador para manejar el registro de nuevos usuarios
@Controller
@RequestMapping("/register")
public class RegisterController extends CategoryExtends {

    @Autowired
    private UserService userService;

    // Ruta hacia el formulario de registrar usuario
    @GetMapping
    public String formAddUser(
            Model model) {

        User user = new User();

        model.addAttribute("user", user);
        generateCategories(model);

        return "formRegisterUser";

    }

    // Metodo para registrar el usuario
    @PostMapping
    public String saveUser(@ModelAttribute("user") @Valid User user,
            BindingResult result,
            Model model) {

        // Si hay errores de validación, renderiza el formulario de registrar usuario
        // con los datos introducidos
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "formRegisterUser";
        }

        // Si el correo del usuario ya existe en la base de datos, agrega un error en el
        // campo email y renderiza el mismo formulario
        if (userService.getUserByEmail(user.getEmail()) == true) {
            model.addAttribute("user", user);
            model.addAttribute("existsUser",
                    "El correo ingresado ya ha sido registrado previamente, intente con otro correo. ");
            return "formRegisterUser";
        }

        // Llama al servicio para guardar al usuario
        userService.save(user);

        // Redirecciona al endpoint que contiene la lista de usuarios
        return "redirect:/users";
    }

}
