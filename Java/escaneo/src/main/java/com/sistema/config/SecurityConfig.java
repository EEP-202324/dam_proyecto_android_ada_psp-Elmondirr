package com.sistema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    // Define un bean para el codificador de contraseñas BCrypt, usado para la encriptación de contraseñas
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de CORS para permitir solicitudes cruzadas desde el origen especificado
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://10.0.2.2")  // Asegúrate de permitir los orígenes adecuados según el entorno de producción o desarrollo
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite métodos HTTP comunes
                .allowedHeaders("*")  // Permite todas las cabeceras
                .allowCredentials(true); // Permite credenciales en las solicitudes CORS
    }

    // Configuración de la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF para simplificar la configuración
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()) // Permitir acceso no autenticado a todas las solicitudes
                .httpBasic(httpBasic -> httpBasic.disable()); // Deshabilitar la autenticación básica HTTP
        return http.build();
    }
}
