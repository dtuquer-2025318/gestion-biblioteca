package com.kinal.biblioteca.auth;

import com.kinal.biblioteca.auth.entity.EstadoUsuario;
import com.kinal.biblioteca.auth.entity.Rol;
import com.kinal.biblioteca.auth.entity.Usuario;
import com.kinal.biblioteca.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository,
                                           PasswordEncoder passwordEncoder,
                                           @Value("${app.admin.default-email}") String adminEmail,
                                           @Value("${app.admin.default-password}") String adminPassword) {
        return args -> {
            if (!usuarioRepository.existsByEmail(adminEmail)) {
                Usuario admin = Usuario.builder()
                        .nombre("Administrador Principal")
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .estado(EstadoUsuario.ACTIVO)
                        .rol(Rol.ADMIN)
                        .build();
                usuarioRepository.save(admin);
                System.out.println(">>> Admin por defecto creado exitosamente: " + adminEmail);
            }
        };
    }
}