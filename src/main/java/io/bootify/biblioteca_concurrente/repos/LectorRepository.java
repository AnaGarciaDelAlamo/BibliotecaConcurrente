package io.bootify.biblioteca_concurrente.repos;

import io.bootify.biblioteca_concurrente.domain.Lector;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LectorRepository extends JpaRepository<Lector, Long> {
}
