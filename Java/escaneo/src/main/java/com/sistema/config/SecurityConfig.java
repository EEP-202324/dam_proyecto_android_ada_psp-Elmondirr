package com.sistema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

/**
 * Configuración de seguridad de la aplicación.
 */
@Configuration // Anota la clase como una configuración de Spring
@EnableWebSecurity // Habilita la seguridad web en la aplicación
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * Bean para codificar las contraseñas usando BCrypt.
     *
     * @return una instancia de BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para definir el servicio de detalles del usuario.
     *
     * @param encoder el codificador de contraseñas BCrypt
     * @return una instancia de InMemoryUserDetailsManager con un usuario en memoria
     */
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Configuración de CORS.
     *
     * @param registry el registro de CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://10.0.2.2")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * Configura la cadena de filtros de seguridad para HTTP.
     *
     * @param http el objeto HttpSecurity
     * @return una instancia de SecurityFilterChain
     * @throws Exception si ocurre algún error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita la protección CSRF
                .authorizeHttpRequests(auth -> auth // Autoriza solicitudes HTTP
                        .requestMatchers("/public/**", "/auth/**").permitAll() // Permite el acceso sin autenticación a estas rutas
                        .requestMatchers("/usuarios/**", "/entradas/**", "/eventos/**").authenticated() // Requiere autenticación para estas rutas
                        .anyRequest().authenticated()) // Requiere autenticación para cualquier otra solicitud
                .httpBasic(httpBasic -> httpBasic // Configura la autenticación básica HTTP
                        .realmName("Backend") // Establece el nombre del realm
                        .authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())); // Punto de entrada personalizado para manejar errores de autenticación
        return http.build(); // Construye la configuración HTTP
    }
}

/**
 * Clase personalizada para el punto de entrada de autenticación básica.
 */
class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    /**
     * Maneja los errores de autenticación.
     *
     * @param request la solicitud HTTP
     * @param response la respuesta HTTP
     * @param authEx la excepción de autenticación
     * @throws IOException si ocurre un error de E/S
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\""); // Agrega cabecera para la autenticación básica
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Establece el estado de respuesta como no autorizado
        response.getWriter().println("HTTP Status 401 - " + authEx.getMessage()); // Escribe un mensaje de error en la respuesta
    }

    /**
     * Establece el nombre del realm después de configurar las propiedades.
     */
    @Override
    public void afterPropertiesSet() {
        setRealmName("Backend"); // Establece el nombre del realm
        super.afterPropertiesSet(); // Llama a la implementación del padre para completar la configuración
    }
}
