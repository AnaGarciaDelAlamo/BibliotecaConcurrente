package io.bootify.biblioteca_concurrente.service;

import io.bootify.biblioteca_concurrente.domain.Bibliotecario;
import io.bootify.biblioteca_concurrente.domain.Lector;
import io.bootify.biblioteca_concurrente.domain.Prestamo;
import io.bootify.biblioteca_concurrente.model.BibliotecarioDTO;
import io.bootify.biblioteca_concurrente.repos.BibliotecarioRepository;
import io.bootify.biblioteca_concurrente.repos.LectorRepository;
import io.bootify.biblioteca_concurrente.repos.PrestamoRepository;
import io.bootify.biblioteca_concurrente.util.NotFoundException;
import io.bootify.biblioteca_concurrente.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BibliotecarioService {

    private final BibliotecarioRepository bibliotecarioRepository;
    private final LectorRepository lectorRepository;
    private final PrestamoRepository prestamoRepository;

    public BibliotecarioService(final BibliotecarioRepository bibliotecarioRepository,
            final LectorRepository lectorRepository, final PrestamoRepository prestamoRepository) {
        this.bibliotecarioRepository = bibliotecarioRepository;
        this.lectorRepository = lectorRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public List<BibliotecarioDTO> findAll() {
        final List<Bibliotecario> bibliotecarios = bibliotecarioRepository.findAll(Sort.by("id"));
        return bibliotecarios.stream()
                .map(bibliotecario -> mapToDTO(bibliotecario, new BibliotecarioDTO()))
                .toList();
    }

    public BibliotecarioDTO get(final Long id) {
        return bibliotecarioRepository.findById(id)
                .map(bibliotecario -> mapToDTO(bibliotecario, new BibliotecarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BibliotecarioDTO bibliotecarioDTO) {
        final Bibliotecario bibliotecario = new Bibliotecario();
        mapToEntity(bibliotecarioDTO, bibliotecario);
        return bibliotecarioRepository.save(bibliotecario).getId();
    }

    public void update(final Long id, final BibliotecarioDTO bibliotecarioDTO) {
        final Bibliotecario bibliotecario = bibliotecarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bibliotecarioDTO, bibliotecario);
        bibliotecarioRepository.save(bibliotecario);
    }

    public void delete(final Long id) {
        bibliotecarioRepository.deleteById(id);
    }

    private BibliotecarioDTO mapToDTO(final Bibliotecario bibliotecario,
            final BibliotecarioDTO bibliotecarioDTO) {
        bibliotecarioDTO.setId(bibliotecario.getId());
        bibliotecarioDTO.setNombre(bibliotecario.getNombre());
        bibliotecarioDTO.setApellido(bibliotecario.getApellido());
        bibliotecarioDTO.setDni(bibliotecario.getDni());
        bibliotecarioDTO.setZona(bibliotecario.getZona());
        bibliotecarioDTO.setLector(bibliotecario.getLector() == null ? null : bibliotecario.getLector().getId());
        return bibliotecarioDTO;
    }

    private Bibliotecario mapToEntity(final BibliotecarioDTO bibliotecarioDTO,
            final Bibliotecario bibliotecario) {
        bibliotecario.setNombre(bibliotecarioDTO.getNombre());
        bibliotecario.setApellido(bibliotecarioDTO.getApellido());
        bibliotecario.setDni(bibliotecarioDTO.getDni());
        bibliotecario.setZona(bibliotecarioDTO.getZona());
        final Lector lector = bibliotecarioDTO.getLector() == null ? null : lectorRepository.findById(bibliotecarioDTO.getLector())
                .orElseThrow(() -> new NotFoundException("lector not found"));
        bibliotecario.setLector(lector);
        return bibliotecario;
    }

    public boolean lectorExists(final Long id) {
        return bibliotecarioRepository.existsByLectorId(id);
    }

    public String getReferencedWarning(final Long id) {
        final Bibliotecario bibliotecario = bibliotecarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Prestamo biblotecarioPrestamo = prestamoRepository.findFirstByBiblotecario(bibliotecario);
        if (biblotecarioPrestamo != null) {
            return WebUtils.getMessage("bibliotecario.prestamo.biblotecario.referenced", biblotecarioPrestamo.getId());
        }
        return null;
    }

}
