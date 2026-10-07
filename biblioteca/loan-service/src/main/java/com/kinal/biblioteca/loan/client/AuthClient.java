package com.kinal.biblioteca.loan.client;

import com.kinal.biblioteca.loan.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public boolean validarUsuarioActivo(String email) {
        try {
            String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            String url = authServiceUrl + "/api/v1/auth/validar?email=" + email;
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            throw new ServiceUnavailableException("No se pudo conectar con Auth Service para validar el estado del usuario.");
        }
    }
}