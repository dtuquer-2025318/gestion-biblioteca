package com.kinal.biblioteca.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "libros", indexes = {
        @Index(name = "idx_libro_isbn", columnList = "isbn"),
        @Index(name = "idx_libro_titulo", columnList = "titulo"),
        @Index(name = "idx_libro_categoria", columnList = "categoria")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false)
    private Integer stockTotal;

    @Column(nullable = false)
    private Integer stockDisponible;
}