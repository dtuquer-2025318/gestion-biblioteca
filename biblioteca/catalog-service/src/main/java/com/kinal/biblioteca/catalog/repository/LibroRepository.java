package com.kinal.biblioteca.catalog.repository;

import com.kinal.biblioteca.catalog.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("SELECT l FROM Libro l WHERE " +
            "(:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
            "(:categoria IS NULL OR LOWER(l.categoria) LIKE LOWER(CONCAT('%', :categoria, '%')))")
    Page<Libro> buscarConFiltros(@Param("titulo") String titulo,
                                 @Param("categoria") String categoria,
                                 Pageable pageable);

    @Modifying
    @Query("UPDATE Libro l SET l.stockDisponible = l.stockDisponible - 1 WHERE l.id = :id AND l.stockDisponible > 0")
    int descontarStockAtomics(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Libro l SET l.stockDisponible = l.stockDisponible + 1 WHERE l.id = :id AND l.stockDisponible < l.stockTotal")
    int restablecerStockAtomics(@Param("id") Long id);
}
