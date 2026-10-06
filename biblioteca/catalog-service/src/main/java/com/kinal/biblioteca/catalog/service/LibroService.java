package com.kinal.biblioteca.catalog.service;

import com.kinal.biblioteca.catalog.dto.request.LibroCreateRequest;
import com.kinal.biblioteca.catalog.dto.request.LibroUpdateRequest;
import com.kinal.biblioteca.catalog.dto.response.LibroResponse;
import com.kinal.biblioteca.catalog.entity.Libro;
import com.kinal.biblioteca.catalog.exception.InsufficientStockException;
import com.kinal.biblioteca.catalog.exception.ResourceNotFoundException;
import com.kinal.biblioteca.catalog.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LibroService {

    private final LibroRepository libroRepository;

    @Transactional(readOnly = true)
    public Page<LibroResponse> listarLibros(String titulo, String categoria, Pageable pageable) {
        return libroRepository.buscarConFiltros(titulo, categoria, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public LibroResponse obtenerPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));
        return mapToResponse(libro);
    }

    @Transactional
    public LibroResponse crearLibro(LibroCreateRequest request) {
        if (libroRepository.existsByIsbn(request.getIsbn())) {
            throw new IllegalArgumentException("Ya existe un libro registrado con el ISBN: " + request.getIsbn());
        }

        Libro libro = Libro.builder()
                .isbn(request.getIsbn())
                .titulo(request.getTitulo())
                .autor(request.getAutor())
                .categoria(request.getCategoria())
                .stockTotal(request.getStockTotal())
                .stockDisponible(request.getStockTotal())
                .build();

        return mapToResponse(libroRepository.save(libro));
    }

    @Transactional
    public LibroResponse actualizarLibro(Long id, LibroUpdateRequest request) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));

        int diferenciaStock = request.getStockTotal() - libro.getStockTotal();
        int nuevoStockDisponible = libro.getStockDisponible() + diferenciaStock;

        if (nuevoStockDisponible < 0) {
            throw new IllegalArgumentException("No se puede reducir el stock total por debajo del stock disponible actual");
        }

        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setCategoria(request.getCategoria());
        libro.setStockTotal(request.getStockTotal());
        libro.setStockDisponible(nuevoStockDisponible);

        return mapToResponse(libroRepository.save(libro));
    }

    @Transactional
    public void eliminarLibro(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro no encontrado con ID: " + id);
        }
        libroRepository.deleteById(id);
    }

    @Transactional
    public void descontarStockInternal(Long id) {
        int filasAfectadas = libroRepository.descontarStockAtomics(id);
        if (filasAfectadas == 0) {
            throw new InsufficientStockException("El libro con ID " + id + " no tiene stock disponible");
        }
    }

    @Transactional
    public void restablecerStockInternal(Long id) {
        int filasAfectadas = libroRepository.restablecerStockAtomics(id);
        if (filasAfectadas == 0) {
            throw new IllegalArgumentException("No se puede restablecer stock: se ha alcanzado el stock total máximo para el ID " + id);
        }
    }

    private LibroResponse mapToResponse(Libro libro) {
        return LibroResponse.builder()
                .id(libro.getId())
                .isbn(libro.getIsbn())
                .titulo(libro.getTitulo())
                .autor(libro.getAutor())
                .categoria(libro.getCategoria())
                .stockTotal(libro.getStockTotal())
                .stockDisponible(libro.getStockDisponible())
                .build();
    }
}