package com.inventory.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.app.auth.PasswordEncoderProvider;
import com.inventory.app.models.dto.UserDto;
import com.inventory.app.models.dto.mapper.DtoMapperUser;
import com.inventory.app.models.entities.Role;
import com.inventory.app.models.entities.User;
import com.inventory.app.models.requests.UserDataRequest;
import com.inventory.app.models.requests.UserPasswordRequest;
import com.inventory.app.repositories.RoleRepository;
import com.inventory.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Instancia del encriptador de contraseñas
    @Autowired
    private PasswordEncoderProvider passwordEncoderProvider;

    // Lista todos los usuarios
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {

        // Se convierte la lista de entidades User a una lista de DTOs UserDto
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(user -> DtoMapperUser.builder().setUser(user).build()).collect(Collectors.toList());

    }

    // Obtiene el usuario por su id
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {

        return userRepository.findById(id).map(user -> DtoMapperUser.builder().setUser(user).build());

    }

    // Obtiene el usuario por su email
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {

        return userRepository.findByEmail(email).map(user -> DtoMapperUser.builder().setUser(user).build());

    }

    // Verifica si existe el usuario registrado
    @Override
    @Transactional(readOnly = true)
    public Boolean getUserByEmail(String email) {

        // Si el usuario con el correo existe, devuelve true, de lo contrario false
        return userRepository.findByEmail(email).isPresent();

    }

    // Guarda el usuario
    @Override
    @Transactional
    public UserDto save(User user) {

        // Encripta la contraseña del usuario
        user.setPassword(passwordEncoderProvider.passwordEncoder().encode(user.getPassword()));

        // Busca el rol 'ROLE_USER' en la base de datos
        Optional<Role> rol = roleRepository.findByName("ROLE_USER");

        // Lanza una excepción si el rol 'ROLE_USER' no existe (muy baja probabilidad)
        if (!rol.isPresent()) {
            throw new IllegalStateException("El rol 'ROLE_USER' no existe en la base de datos");
        }

        // Crea una nueva lista que contiene objetos Role
        List<Role> roles = new ArrayList<>();

        // Si el rol existe, lo añade a la lista
        if (rol.isPresent()) {
            roles.add(rol.orElseThrow());
        }

        // Agrega el rol de ADMIN si isAdmin es true
        if (user.isAdmin()) {
            Optional<Role> optionalAdmin = roleRepository.findByName("ROLE_ADMIN");
            if (optionalAdmin.isPresent()) {
                roles.add(optionalAdmin.orElseThrow());
            }
        }

        // Agrega el rol de MANAGER si isManager es true
        if (user.isManager()) {
            Optional<Role> optionalManager = roleRepository.findByName("ROLE_MANAGER");
            if (optionalManager.isPresent()) {
                roles.add(optionalManager.orElseThrow());
            }
        }

        // Asigna los roles al usuario
        user.setRoles(roles);
        return DtoMapperUser.builder().setUser(userRepository.save(user)).build();

    }

    // Actualiza los datos del usuario
    @Override
    @Transactional
    public Optional<UserDto> update(UserDataRequest userDataRequest, String email) {
        // Busca el usuario por su correo electrónico en el repositorio
        Optional<User> userById = userRepository.findByEmail(email);

        User user = null;

        // Verifica si el usuario existe en el repositorio
        if (userById.isPresent()) {
            // Si el usuario existe, obtiene la entidad User
            User userData = userById.orElseThrow();

            // Actualiza los campos firstname y lastname con los datos del request
            userData.setFirstname(userDataRequest.getFirstname());
            userData.setLastname(userDataRequest.getLastname());

            // Guarda los cambios en el repositorio y actualiza user con la entidad
            // actualizada
            user = userRepository.save(userData);
        }

        // Convierte la entidad User actualizada a un DTO y la devuelve envuelta en un
        // Optional
        return Optional.ofNullable(DtoMapperUser.builder().setUser(user).build());

    }

    // Actualiza la contraseña del usuario
    @Override
    @Transactional
    public Boolean validateAndUpdatePassword(UserPasswordRequest userPasswordRequest, String email) {

        // Busca el usuario por su correo electrónico en el repositorio
        Optional<User> userByEmail = userRepository.findByEmail(email);

        // Verifica si el usuario existe y si la contraseña actual proporcionada
        // coincide con la almacenada
        if (userByEmail.isPresent()
                && passwordEncoderProvider.passwordEncoder().matches(userPasswordRequest.getCurrentPassword(),
                        userByEmail.get().getPassword())) {

            // Si coincide, obtiene la entidad User
            User user = userByEmail.orElseThrow();

            // Encripta y actualiza la nueva contraseña
            user.setPassword(passwordEncoderProvider.passwordEncoder().encode(userPasswordRequest.getNewPassword()));

            // Guarda los cambios en el repositorio
            userRepository.save(user);

            // Retorna true si la contraseña fue actualizada con éxito
            return true;

        } else {
            // Retorna false si no coincide la contraseña actual o el usuario no existe
            return false;
        }

    }

    // Elimina definitivamente el usuario por su email
    @Override
    @Transactional
    public void remove(Long id) {

        // Este método elimina al usuario desde la base de datos
        userRepository.deleteById(id);

    }

}
