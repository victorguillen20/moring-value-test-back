package com.morning.torneo.infrastructure.persistence.adapter;

import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import com.morning.torneo.infrastructure.persistence.entity.EspecieEntity;
import com.morning.torneo.infrastructure.persistence.repository.EspecieJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EspecieRepositoryAdapter implements EspecieRepositoryPort {

    private final EspecieJpaRepository jpaRepository;

    public EspecieRepositoryAdapter(EspecieJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Especie save(Especie especie) {
        EspecieEntity entity = toEntity(especie);
        EspecieEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Especie> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Especie> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Especie> findByNombre(String nombre) {
        return jpaRepository.findByNombre(nombre)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return jpaRepository.existsByNombre(nombre);
    }

    private Especie toDomain(EspecieEntity entity) {
        Especie especie = new Especie();
        especie.setId(entity.getId());
        especie.setNombre(entity.getNombre());
        especie.setNivelPoder(entity.getNivelPoder());
        especie.setHabilidadEspecial(entity.getHabilidadEspecial());
        especie.setFechaCreacion(entity.getFechaCreacion());
        return especie;
    }

    private EspecieEntity toEntity(Especie especie) {
        EspecieEntity entity = new EspecieEntity(
                especie.getNombre(),
                especie.getNivelPoder(),
                especie.getHabilidadEspecial(),
                especie.getFechaCreacion()
        );
        entity.setId(especie.getId());
        return entity;
    }
}
