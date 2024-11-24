package com.inventory.app.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventory.app.models.dto.UserDto;
import com.inventory.app.models.requests.UserDataRequest;
import com.inventory.app.models.requests.UserPasswordRequest;
import com.inventory.app.services.UserService;

import jakarta.validation.Valid;

// Controlador con endpoints relacionados al perfil del usuario
@Controller
@RequestMapping("/profile")
public class ProfileController extends CategoryExtends {

    @Autowired
    private UserService userService;

    // Ruta hacia el perfil del usuario
    // RequestParam sirve para realizar alguna operacion si el parametro definido
    // aparece en el endpoint
    // required con el valor false indica que no es obligatorio que siempre aparezca
    // en la endpoint
    // authentication sirve para obtener datos del usuario que ha iniciado sesión
    @GetMapping
    public String getProfileUser(@RequestParam(value = "passwordUpdated", required = false) String passwordUpdated,
            Authentication authentication,
            Model model) {

        // Obtiene el nombre (el email) del usuario que ha iniciado sesión
        String username = authentication.getName();

        // Busca al usuario por su email
        Optional<UserDto> user = userService.findByEmail(username);

        // Añade los datos del usuario al modelo
        model.addAttribute("firstname", user.orElseThrow().getFirstname());
        model.addAttribute("lastname", user.orElseThrow().getLastname());
        model.addAttribute("email", user.orElseThrow().getEmail());

        // Añade los roles del usuario al modelo (existen 4 combinaciones)
        if (user.orElseThrow().isManager() == true && user.orElseThrow().isAdmin() == true) {
            model.addAttribute("roles", "MANAGER - ADMIN - USER");
        }

        if (user.orElseThrow().isManager() == true) {
            model.addAttribute("roles", "MANAGER - USER");
        }

        if (user.orElseThrow().isAdmin() == true) {
            model.addAttribute("roles", "ADMIN - USER");
        }

        if (user.orElseThrow().isManager() != true && user.orElseThrow().isAdmin() != true) {
            model.addAttribute("roles", "USER");
        }

        // Añade un mensaje si el usuario ha cambiado su contraseña
        if (passwordUpdated != null) {
            model.addAttribute("passwordUpdated",
                    "Se ha actualizado la contraseña, puede cerrar sesión e iniciar sesión con la nueva contraseña.");
        }

        generateCategories(model);

        // Devuelve la pagina profileUser
        return "profileUser";

    }

    // Ruta hacia el formulario para actualizar el perfil del usuario
    @GetMapping("/update")
    public String formEditProfileUser(Authentication authentication, Model model) {

        String username = authentication.getName();
        Optional<UserDto> user = userService.findByEmail(username);

        // Añade los datos del usuario al modelo
        model.addAttribute("user", user);

        generateCategories(model);
        return "formEditProfileUser";

    }

    // Metodo para actualizar el perfil del usuario
    @PostMapping("/update")
    public String updateUserData(
            @ModelAttribute("user") @Valid UserDataRequest userDataRequest,
            BindingResult result,
            Authentication authentication,
            Model model) {

        // Si hay errores de validación, renderiza la misma página
        if (result.hasErrors()) {
            model.addAttribute("user", userDataRequest);
            generateCategories(model);
            return "formEditProfileUser";
        }

        // Obtiene el email del usuario y lo utiliza para actualizar el perfil del
        // usuario
        String email = authentication.getName();
        userService.update(userDataRequest, email);

        // Redirecciona al perfil del usuario
        return "redirect:/profile";
    }

    // Ruta hacia el formulario para cambiar la contraseña del usuario
    @GetMapping("/update/password")
    public String formEditUserPassword(Authentication authentication, Model model) {

        // No se obtiene el email del usuario para obtener la contraseña actual
        // String username = authentication.getName();
        // Optional<UserDto> user = userService.findByEmail(username);

        // Hay una probabilidad muy baja de que el usuario no exista
        // model.addAttribute("user", user.orElseThrow(() -> new RuntimeException("User
        // not found")));

        // Agrega un nuevo objeto UserPasswordRequest vacio para manejar los campos
        // orientados a la contraseña del usuario
        model.addAttribute("userPasswordRequest", new UserPasswordRequest());

        generateCategories(model);

        return "formEditPasswordUser";

    }

    // Metodo para validar la contraseña anterior del usuario y actualizar la nueva
    // contraseña del usuario
    @PostMapping("/update/password")
    public String updateUserPassword(
            @ModelAttribute("userPasswordRequest") @Valid UserPasswordRequest userPasswordRequest,
            BindingResult result,
            Authentication authentication,
            Model model) {

        // Verifica si hubo errores como campos vacios
        if (result.hasErrors()) {
            model.addAttribute("userPasswordRequest", userPasswordRequest);
            generateCategories(model);
            return "formEditPasswordUser";
        }

        // Obtiene el username (email) del usuario que ha iniciado sesión
        String username = authentication.getName();

        // Valida que si la contraseña se tiene que actualizar (devuelve un true o
        // false)
        boolean passwordUpdated = userService.validateAndUpdatePassword(userPasswordRequest, username);

        // Si la contraseña se ha actualizado
        if (passwordUpdated == true) {

            // Redirecciona al endpoint profile y añade un parametro al endpoint
            return "redirect:/profile?passwordUpdated=true";

        } else {

            // De lo contrario añade un atributo error y renderiza la pagina
            // formEditPasswordUser
            model.addAttribute("error", "La contraseña anterior no es correcta");
            return "formEditPasswordUser";

        }

    }

}
