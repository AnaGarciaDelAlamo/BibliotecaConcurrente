package io.bootify.biblioteca_concurrente.service;

import io.bootify.biblioteca_concurrente.domain.Bibliotecario;
import io.bootify.biblioteca_concurrente.domain.Lector;
import io.bootify.biblioteca_concurrente.domain.Prestamo;
import io.bootify.biblioteca_concurrente.model.LectorDTO;
import io.bootify.biblioteca_concurrente.repos.BibliotecarioRepository;
import io.bootify.biblioteca_concurrente.repos.LectorRepository;
import io.bootify.biblioteca_concurrente.repos.PrestamoRepository;
import io.bootify.biblioteca_concurrente.util.NotFoundException;
import io.bootify.biblioteca_concurrente.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LectorService {

    private final LectorRepository lectorRepository;
    private final PrestamoRepository prestamoRepository;
    private final BibliotecarioRepository bibliotecarioRepository;

    public LectorService(final LectorRepository lectorRepository,
            final PrestamoRepository prestamoRepository,
            final BibliotecarioRepository bibliotecarioRepository) {
        this.lectorRepository = lectorRepository;
        this.prestamoRepository = prestamoRepository;
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    public List<LectorDTO> findAll() {
        final List<Lector> lectors = lectorRepository.findAll(Sort.by("id"));
        return lectors.stream()
                .map(lector -> mapToDTO(lector, new LectorDTO()))
                .toList();
    }

    public LectorDTO get(final Long id) {
        return lectorRepository.findById(id)
                .map(lector -> mapToDTO(lector, new LectorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LectorDTO lectorDTO) {
        final Lector lector = new Lector();
        mapToEntity(lectorDTO, lector);
        return lectorRepository.save(lector).getId();
    }

    public void update(final Long id, final LectorDTO lectorDTO) {
        final Lector lector = lectorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(lectorDTO, lector);
        lectorRepository.save(lector);
    }

    public void delete(final Long id) {
        lectorRepository.deleteById(id);
    }

    private LectorDTO mapToDTO(final Lector lector, final LectorDTO lectorDTO) {
        lectorDTO.setId(lector.getId());
        lectorDTO.setNombre(lector.getNombre());
        lectorDTO.setApellido(lector.getApellido());
        lectorDTO.setDni(lector.getDni());
        return lectorDTO;
    }

    private Lector mapToEntity(final LectorDTO lectorDTO, final Lector lector) {
        lector.setNombre(lectorDTO.getNombre());
        lector.setApellido(lectorDTO.getApellido());
        lector.setDni(lectorDTO.getDni());
        return lector;
    }

    public String getReferencedWarning(final Long id) {
        final Lector lector = lectorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Prestamo lectorPrestamo = prestamoRepository.findFirstByLector(lector);
        if (lectorPrestamo != null) {
            return WebUtils.getMessage("lector.prestamo.lector.referenced", lectorPrestamo.getId());
        }
        final Bibliotecario lectorBibliotecario = bibliotecarioRepository.findFirstByLector(lector);
        if (lectorBibliotecario != null) {
            return WebUtils.getMessage("lector.bibliotecario.lector.referenced", lectorBibliotecario.getId());
        }
        return null;
    }

}
