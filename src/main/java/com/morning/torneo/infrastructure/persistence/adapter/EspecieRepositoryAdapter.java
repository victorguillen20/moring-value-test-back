package com.morning.torneo.infrastructure.persistence.adapter;

import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import com.morning.torneo.infrastructure.persistence.entity.EspecieEntity;
import com.morning.torneo.infrastructure.persistence.mapper.EspecieMapper;
import com.morning.torneo.infrastructure.persistence.repository.EspecieJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EspecieRepositoryAdapter implements EspecieRepositoryPort {

    private final EspecieJpaRepository jpaRepository;

    public EspecieRepositoryAdapter(EspecieJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Especie save(Especie especie) {
        EspecieEntity entity = EspecieMapper.toEntity(especie);
        EspecieEntity saved = jpaRepository.save(entity);
        return EspecieMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especie> findAll() {
        return jpaRepository.findAll().stream()
                .map(EspecieMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Especie> findById(Long id) {
        return jpaRepository.findById(id)
                .map(EspecieMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Especie> findByNombre(String nombre) {
        return jpaRepository.findByNombre(nombre)
                .map(EspecieMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return jpaRepository.existsByNombre(nombre);
    }
}
