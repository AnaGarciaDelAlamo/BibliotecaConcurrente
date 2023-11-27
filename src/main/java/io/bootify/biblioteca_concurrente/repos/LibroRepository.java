package io.bootify.biblioteca_concurrente.repos;

import io.bootify.biblioteca_concurrente.domain.Libro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LibroRepository extends JpaRepository<Libro, Long> {
}
