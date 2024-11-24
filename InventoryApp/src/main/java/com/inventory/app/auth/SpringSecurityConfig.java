package com.inventory.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.inventory.app.services.UserDetailsServiceImpl;

// Configuración actualizada de seguridad de Spring
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

        // Servicio para los detalles de usuario
        @Autowired
        private UserDetailsServiceImpl jpaUserDetailsService;

        // Instancia del tipo de cifrado de contraseña
        @Autowired
        private PasswordEncoderProvider passwordEncoderProvider;

        // Configura el AuthenticationManager
        // authenticationConfiguration representa la configuración de autenticación
        @Bean
        AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authenticationConfiguration) throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        // Proveedor de autenticación que utiliza DAO para consultar una fuente de datos
        @Bean
        DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
                auth.setUserDetailsService(jpaUserDetailsService);
                auth.setPasswordEncoder(passwordEncoderProvider.passwordEncoder());
                return auth;

        }

        // Configura las rutas y permisos segun los roles asignados
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.authorizeHttpRequests(authorize -> authorize
                                // Permitir a todos el acceso al contenido estatico
                                .requestMatchers("/css/**", "/img/**", "/icons/**", "/js/**").permitAll()

                                // Permitir a todos el acceso al endpoint de iniciar sesión
                                .requestMatchers(HttpMethod.GET, "/", "/login").permitAll()

                                // A TODOS LOS USUARIOS QUE HAYAN INICIADO SESION...

                                // La ruta definida en el controlador permite el acceso a las imagenes subidas
                                .requestMatchers(HttpMethod.GET, "/image/**").authenticated()

                                // LOS USUARIOS CON EL ROL DE USER PUEDEN:

                                // - visualizar la lista de productos, hacer una busqueda de productos o
                                // visualizar un producto
                                .requestMatchers(HttpMethod.GET, "/products", "/products/search",
                                                "/products/details/**", "/products/category/**")
                                .hasRole("USER")

                                // - ver las categorias registradas, pero no puede modificarlas
                                .requestMatchers(HttpMethod.GET, "/categories").hasRole("USER")

                                // - visualizar su perfil y los formularios para editar perfil y cambiar de
                                // contraseña
                                .requestMatchers(HttpMethod.GET, "/profile", "/profile/update",
                                                "/profile/update/password")
                                .hasRole("USER")

                                // - Modificar su perfil y cambiar su contraseña
                                .requestMatchers(HttpMethod.POST, "/profile/update", "/profile/update/password")
                                .hasRole("USER")

                                // LOS USUARIOS CON EL ROL DE ADMIN PUEDEN:

                                // - visualizar los formularios de agregar y editar categoria
                                .requestMatchers(HttpMethod.GET, "/categories/add", "/categories/edit/**")
                                .hasRole("ADMIN")

                                // - agregar, editar y eliminar categorias
                                .requestMatchers(HttpMethod.POST, "/categories/add", "/categories/edit/**",
                                                "/categories/delete/**")
                                .hasRole("ADMIN")

                                // - visualizar los formularios de agregar y editar categoria
                                .requestMatchers(HttpMethod.GET, "/products/add", "/products/edit/**").hasRole("ADMIN")

                                // - agregar, editar y eliminar productos
                                .requestMatchers(HttpMethod.POST, "/products/add", "/products/edit/**",
                                                "/products/delete/**")
                                .hasRole("ADMIN")

                                // LOS USUARIOS CON EL ROL DE MANAGER PUEDEN:

                                // - ver la lista de usuarios en el sistema
                                .requestMatchers(HttpMethod.GET, "/users").hasRole("MANAGER")

                                // - ver el formulario de registrar usuario
                                .requestMatchers(HttpMethod.GET, "/register").hasRole("MANAGER")

                                // - registrar otros usuarios
                                .requestMatchers(HttpMethod.POST, "/register").hasRole("MANAGER")

                                // - eliminar usuarios existentes
                                .requestMatchers(HttpMethod.POST, "/users/delete/**").hasRole("MANAGER")

                                // Cualquier otro endpoint que no haya sido definido, se podrá acceder si el
                                // usuario inició sesión
                                .anyRequest().authenticated())

                                // Deshabilita la configuración de CRFC (Cross Site Request Forgery)
                                // .csrf(crfc -> crfc.disable())

                                // Define el endpoint de error cuando se genera un error 403 (acceso no
                                // autorizado)
                                .exceptionHandling(exception -> exception.accessDeniedPage("/403"))

                                // Configura el endpoint hacia el formulario de iniciar sesion
                                .formLogin(formlogin -> formlogin
                                                // Endpoint de login
                                                .loginPage("/login")
                                                // Endpoint para procesar el inicio de sesión
                                                .loginProcessingUrl("/login")
                                                // Redirige al endpoint luego de iniciar sesión
                                                .defaultSuccessUrl("/products", true)
                                                // endpoint de error en caso de fallo
                                                .failureUrl("/login?error=true")
                                                // Nombre del campo del nombre del usuario (se utiliza email)
                                                .usernameParameter("email")
                                                // Nombre del campo de la contraseña
                                                .passwordParameter("password"))

                                // Configura el metodo para cerrar sesion
                                .logout(logout -> logout
                                                // Invalida la sesión
                                                .invalidateHttpSession(true)
                                                // Limpia la autenticación
                                                .clearAuthentication(true)
                                                // Activa el cierre de sesión al ir al endpoint
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                // Endpoint de exito al haber cerrado sesión
                                                .logoutSuccessUrl("/login?logout=true")
                                                .permitAll())
                                .build();
        }
}

// PAGINA PARA ENCRIPTAR UNA CONTRASEÑA
// https://bcrypt-generator.com/