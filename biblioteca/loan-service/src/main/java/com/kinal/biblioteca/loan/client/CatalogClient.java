package com.kinal.biblioteca.loan.client;

import com.kinal.biblioteca.loan.exception.LoanOperationException;
import com.kinal.biblioteca.loan.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CatalogClient {

    private final RestTemplate restTemplate;

    @Value("${catalog.service.url}")
    private String catalogServiceUrl;

    public void descontarStock(Long libroId) {
        String url = catalogServiceUrl + "/api/v1/libros/" + libroId + "/descontar-stock";
        executePatch(url, "El libro seleccionado no cuenta con stock disponible.");
    }

    public void restablecerStock(Long libroId) {
        String url = catalogServiceUrl + "/api/v1/libros/" + libroId + "/restablecer-stock";
        executePatch(url, "No se pudo restaurar el stock del libro en Catalog Service.");
    }

    private void executePatch(String url, String defaultErrorMessage) {
        try {
            String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            throw new LoanOperationException(defaultErrorMessage);
        } catch (Exception e) {
            throw new ServiceUnavailableException("No se pudo comunicar con Catalog Service. Intente más tarde.");
        }
    }
}