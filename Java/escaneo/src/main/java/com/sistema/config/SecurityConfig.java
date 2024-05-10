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
import java.io.IOException;

// Anota la clase como una configuración de Spring y habilita la seguridad web
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean para codificar las contraseñas usando BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Configura la cadena de filtros de seguridad para HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilita la protección CSRF
                .authorizeHttpRequests(auth -> auth  // Autoriza solicitudes HTTP
                        .requestMatchers("/public/**", "/auth/**").permitAll()  // Permite el acceso sin autenticación a estas rutas
                        .requestMatchers("/usuarios/**", "/entradas/**", "/eventos/**").authenticated()  // Requiere autenticación para estas rutas
                        .anyRequest().authenticated())  // Requiere autenticación para cualquier otra solicitud
                .httpBasic(httpBasic -> httpBasic  // Configura la autenticación básica HTTP
                        .realmName("YourApp")  // Establece el nombre del realm
                        .authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));  // Punto de entrada personalizado para manejar errores de autenticación
        return http.build();  // Construye la configuración HTTP
    }
}

// Clase personalizada para el punto de entrada de autenticación básica
class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");  // Agrega cabecera para la autenticación básica
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Establece el estado de respuesta como no autorizado
        response.getWriter().println("HTTP Status 401 - " + authEx.getMessage());  // Escribe un mensaje de error en la respuesta
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Backend");  // Establece el nombre del realm
        super.afterPropertiesSet();  // Llama a la implementación del padre para completar la configuración
    }
}
