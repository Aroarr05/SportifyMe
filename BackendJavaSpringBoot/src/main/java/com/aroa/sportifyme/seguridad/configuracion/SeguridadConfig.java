package com.aroa.sportifyme.seguridad.configuracion;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.aroa.sportifyme.seguridad.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SeguridadConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configurar autorizaciones
            .authorizeHttpRequests(auth -> auth
            // Rutas PÚBLICAS (sin autenticación)
            .requestMatchers(
                "/",                    
                "/favicon.ico",        
                "/health",             
                "/api/test",           
                "/api/auth/**",        
                "/actuator/health",    
                "/error"               
            ).permitAll()
            
            // Desafíos: lectura pública, escritura para usuarios autenticados
            .requestMatchers(HttpMethod.GET, "/api/desafios/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/desafios/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/desafios/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/desafios/**").hasRole("ADMIN")
            
            // Usuarios: perfil público, gestión para admins
            .requestMatchers(HttpMethod.GET, "/api/usuarios/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
            
            // ✅ NUEVO: Ranking público, otros progresos requieren autenticación
            .requestMatchers(HttpMethod.GET, "/api/progresos/ranking/**").permitAll()
            .requestMatchers("/api/progresos/**").authenticated()
            
            // Participaciones requieren autenticación
            .requestMatchers("/api/participaciones/**").authenticated()
            
            // Admin solo para administradores
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            
            // Cualquier otra ruta requiere autenticación
            .anyRequest().authenticated()
            )
            
            // Configurar sesiones sin estado (STATELESS para JWT)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Añadir filtro JWT antes del filtro de autenticación
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Configuración CORS para desarrollo
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // En producción, especifica dominios
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}