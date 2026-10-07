package com.kinal.biblioteca.loan.repository;

import com.kinal.biblioteca.loan.entity.EstadoPrestamo;
import com.kinal.biblioteca.loan.entity.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Page<Prestamo> findByUsuarioEmail(String usuarioEmail, Pageable pageable);

    boolean existsByUsuarioEmailAndLibroIdAndEstado(String usuarioEmail, Long libroId, EstadoPrestamo estado);
}