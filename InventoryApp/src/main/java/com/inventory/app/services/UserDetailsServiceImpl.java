package com.inventory.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.app.models.entities.User;
import com.inventory.app.repositories.UserRepository;

// Servicio para la gestión de detalles de usuario en la autenticación.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    // Carga un usuario por su nombre de usuario (correo electrónico).
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Busca el usuario por correo electrónico en el repositorio
        Optional<User> userByEmail = userRepository.findByEmail(email);
        // userRepository.findByEmail(email);

        // Si el usuario no está presente, lanza una excepción
        if (!userByEmail.isPresent()) {
            throw new UsernameNotFoundException("El usuario con el correo " + email + " no existe en el sistema");
        }

        // Obtiene el usuario
        User user = userByEmail.orElseThrow();

        // Mapea los roles del usuario a autoridades de Spring Security
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());

        // Retorna un UserDetails de Spring Security con las propiedades del usuario
        // email, password, cuenta habilitada, cuenta no expirada, credenciales no
        // expiradas, cuenta no bloqueada y autoridades
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true,
                true,
                true,
                true,
                authorities);

    }

}
