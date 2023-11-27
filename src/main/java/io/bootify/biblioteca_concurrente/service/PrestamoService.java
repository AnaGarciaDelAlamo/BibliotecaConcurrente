package io.bootify.biblioteca_concurrente.service;

import io.bootify.biblioteca_concurrente.domain.Bibliotecario;
import io.bootify.biblioteca_concurrente.domain.Lector;
import io.bootify.biblioteca_concurrente.domain.Libro;
import io.bootify.biblioteca_concurrente.domain.Prestamo;
import io.bootify.biblioteca_concurrente.model.PrestamoDTO;
import io.bootify.biblioteca_concurrente.repos.BibliotecarioRepository;
import io.bootify.biblioteca_concurrente.repos.LectorRepository;
import io.bootify.biblioteca_concurrente.repos.LibroRepository;
import io.bootify.biblioteca_concurrente.repos.PrestamoRepository;
import io.bootify.biblioteca_concurrente.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final LectorRepository lectorRepository;
    private final BibliotecarioRepository bibliotecarioRepository;

    public PrestamoService(final PrestamoRepository prestamoRepository,
            final LibroRepository libroRepository, final LectorRepository lectorRepository,
            final BibliotecarioRepository bibliotecarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.lectorRepository = lectorRepository;
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    public List<PrestamoDTO> findAll() {
        final List<Prestamo> prestamoes = prestamoRepository.findAll(Sort.by("id"));
        return prestamoes.stream()
                .map(prestamo -> mapToDTO(prestamo, new PrestamoDTO()))
                .toList();
    }

    public PrestamoDTO get(final Long id) {
        return prestamoRepository.findById(id)
                .map(prestamo -> mapToDTO(prestamo, new PrestamoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PrestamoDTO prestamoDTO) {
        final Prestamo prestamo = new Prestamo();
        mapToEntity(prestamoDTO, prestamo);
        return prestamoRepository.save(prestamo).getId();
    }

    public void update(final Long id, final PrestamoDTO prestamoDTO) {
        final Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(prestamoDTO, prestamo);
        prestamoRepository.save(prestamo);
    }

    public void delete(final Long id) {
        prestamoRepository.deleteById(id);
    }

    private PrestamoDTO mapToDTO(final Prestamo prestamo, final PrestamoDTO prestamoDTO) {
        prestamoDTO.setId(prestamo.getId());
        prestamoDTO.setFechaPrestamo(prestamo.getFechaPrestamo());
        prestamoDTO.setFechaDevolucion(prestamo.getFechaDevolucion());
        prestamoDTO.setLibro(prestamo.getLibro() == null ? null : prestamo.getLibro().getId());
        prestamoDTO.setLector(prestamo.getLector() == null ? null : prestamo.getLector().getId());
        prestamoDTO.setBiblotecario(prestamo.getBiblotecario() == null ? null : prestamo.getBiblotecario().getId());
        return prestamoDTO;
    }

    private Prestamo mapToEntity(final PrestamoDTO prestamoDTO, final Prestamo prestamo) {
        prestamo.setFechaPrestamo(prestamoDTO.getFechaPrestamo());
        prestamo.setFechaDevolucion(prestamoDTO.getFechaDevolucion());
        final Libro libro = prestamoDTO.getLibro() == null ? null : libroRepository.findById(prestamoDTO.getLibro())
                .orElseThrow(() -> new NotFoundException("libro not found"));
        prestamo.setLibro(libro);
        final Lector lector = prestamoDTO.getLector() == null ? null : lectorRepository.findById(prestamoDTO.getLector())
                .orElseThrow(() -> new NotFoundException("lector not found"));
        prestamo.setLector(lector);
        final Bibliotecario biblotecario = prestamoDTO.getBiblotecario() == null ? null : bibliotecarioRepository.findById(prestamoDTO.getBiblotecario())
                .orElseThrow(() -> new NotFoundException("biblotecario not found"));
        prestamo.setBiblotecario(biblotecario);
        return prestamo;
    }

}
