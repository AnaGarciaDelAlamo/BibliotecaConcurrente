package io.bootify.biblioteca_concurrente.repos;

import io.bootify.biblioteca_concurrente.domain.Bibliotecario;
import io.bootify.biblioteca_concurrente.domain.Lector;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, Long> {

    Bibliotecario findFirstByLector(Lector lector);

    boolean existsByLectorId(Long id);

}
