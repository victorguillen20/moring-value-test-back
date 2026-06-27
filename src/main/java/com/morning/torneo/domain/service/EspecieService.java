package com.morning.torneo.domain.service;

import com.morning.torneo.domain.exception.EspecieInvalidaException;
import com.morning.torneo.domain.exception.EspecieYaExisteException;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.in.EspecieUseCase;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;

public class EspecieService implements EspecieUseCase {

    private final EspecieRepositoryPort repository;

    public EspecieService(EspecieRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Especie registrar(Especie especie) {
        String nombre = especie.getNombre();
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new EspecieInvalidaException("El nombre de la especie es obligatorio");
        }
        if (nombre.length() > 50) {
            throw new EspecieInvalidaException("El nombre no puede tener mas de 50 caracteres");
        }
        if (especie.getNivelPoder() < 1 || especie.getNivelPoder() > 1000) {
            throw new EspecieInvalidaException("El nivel de poder debe estar entre 1 y 1000");
        }
        String habilidad = especie.getHabilidadEspecial();
        if (habilidad == null || habilidad.trim().isEmpty()) {
            throw new EspecieInvalidaException("La habilidad especial no puede estar vacia");
        }
        if (habilidad.length() > 100) {
            throw new EspecieInvalidaException("La habilidad especial no puede tener mas de 100 caracteres");
        }

        especie.setNombre(nombre.trim());
        especie.setHabilidadEspecial(habilidad.trim());

        if (repository.existsByNombre(especie.getNombre())) {
            throw new EspecieYaExisteException("Ya existe una especie con el nombre: " + especie.getNombre());
        }

        especie.setFechaCreacion(LocalDateTime.now());

        return repository.save(especie);
    }

    @Override
    public List<Especie> listar() {
        return repository.findAll();
    }
}
