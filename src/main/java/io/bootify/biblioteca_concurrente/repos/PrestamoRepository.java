package io.bootify.biblioteca_concurrente.repos;

import io.bootify.biblioteca_concurrente.domain.Bibliotecario;
import io.bootify.biblioteca_concurrente.domain.Lector;
import io.bootify.biblioteca_concurrente.domain.Libro;
import io.bootify.biblioteca_concurrente.domain.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Prestamo findFirstByLibro(Libro libro);

    Prestamo findFirstByLector(Lector lector);

    Prestamo findFirstByBiblotecario(Bibliotecario bibliotecario);

}
