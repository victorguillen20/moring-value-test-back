package com.morning.torneo.domain.port.out;

import com.morning.torneo.domain.model.Especie;
import java.util.List;
import java.util.Optional;

public interface EspecieRepositoryPort {

    Especie save(Especie especie);

    List<Especie> findAll();

    Optional<Especie> findById(Long id);

    Optional<Especie> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
