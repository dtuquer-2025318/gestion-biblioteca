package com.kinal.biblioteca.auth.service;

import com.kinal.biblioteca.auth.dto.request.LoginRequest;
import com.kinal.biblioteca.auth.dto.request.RegisterRequest;
import com.kinal.biblioteca.auth.dto.response.AuthResponse;
import com.kinal.biblioteca.auth.entity.EstadoUsuario;
import com.kinal.biblioteca.auth.entity.Rol;
import com.kinal.biblioteca.auth.entity.Usuario;
import com.kinal.biblioteca.auth.repository.UsuarioRepository;
import com.kinal.biblioteca.auth.security.JwtProvider;
import com.kinal.biblioteca.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("El email " + request.getEmail() + " ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .estado(EstadoUsuario.ACTIVO)
                .rol(Rol.LECTOR)
                .build();

        Usuario savedUser = usuarioRepository.save(usuario);
        String token = jwtProvider.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(savedUser.getId())
                .nombre(savedUser.getNombre())
                .email(savedUser.getEmail())
                .rol(savedUser.getRol().name())
                .estado(savedUser.getEstado().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtProvider.generateToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .estado(usuario.getEstado().name())
                .build();
    }
}
