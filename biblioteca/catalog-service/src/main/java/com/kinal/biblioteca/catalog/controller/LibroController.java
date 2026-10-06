package com.kinal.biblioteca.catalog.controller;

import com.kinal.biblioteca.catalog.dto.request.LibroCreateRequest;
import com.kinal.biblioteca.catalog.dto.request.LibroUpdateRequest;
import com.kinal.biblioteca.catalog.dto.response.LibroResponse;
import com.kinal.biblioteca.catalog.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @GetMapping
    public ResponseEntity<Page<LibroResponse>> listarLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria,
            Pageable pageable) {
        return ResponseEntity.ok(libroService.listarLibros(titulo, categoria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody LibroCreateRequest request) {
        return new ResponseEntity<>(libroService.crearLibro(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> actualizarLibro(@PathVariable Long id, @Valid @RequestBody LibroUpdateRequest request) {
        return ResponseEntity.ok(libroService.actualizarLibro(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints internos de comunicación entre microservicios
    @PatchMapping("/{id}/descontar-stock")
    public ResponseEntity<Void> descontarStock(@PathVariable Long id) {
        libroService.descontarStockInternal(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/restablecer-stock")
    public ResponseEntity<Void> restablecerStock(@PathVariable Long id) {
        libroService.restablecerStockInternal(id);
        return ResponseEntity.ok().build();
    }
}
