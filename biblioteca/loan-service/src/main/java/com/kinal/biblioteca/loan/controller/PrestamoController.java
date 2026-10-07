package com.kinal.biblioteca.loan.controller;

import com.kinal.biblioteca.loan.dto.request.PrestamoCreateRequest;
import com.kinal.biblioteca.loan.dto.response.PrestamoResponse;
import com.kinal.biblioteca.loan.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoResponse> crearPrestamo(@Valid @RequestBody PrestamoCreateRequest request) {
        return new ResponseEntity<>(prestamoService.crearPrestamo(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponse> devolverLibro(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    @GetMapping("/mis-prestamos")
    public ResponseEntity<Page<PrestamoResponse>> listarMisPrestamos(Pageable pageable) {
        return ResponseEntity.ok(prestamoService.listarMisPrestamos(pageable));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PrestamoResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(prestamoService.listarTodos(pageable));
    }
}