package com.inventory.app.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// Proveedor de codificación de contraseñas
// Utiliza BCrypt para cifrar las contraseñas
@Component
@Configuration
public class PasswordEncoderProvider {

    // Configura el tipo de cifrado de contraseña
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Devuelve una instancia de BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

}
