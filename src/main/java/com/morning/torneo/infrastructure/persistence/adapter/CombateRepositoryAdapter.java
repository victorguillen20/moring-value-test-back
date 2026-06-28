package com.morning.torneo.infrastructure.persistence.adapter;

import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.infrastructure.persistence.entity.CombateEntity;
import com.morning.torneo.infrastructure.persistence.entity.EspecieEntity;
import com.morning.torneo.infrastructure.persistence.mapper.EspecieMapper;
import com.morning.torneo.infrastructure.persistence.repository.CombateJpaRepository;
import com.morning.torneo.infrastructure.persistence.repository.EspecieJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CombateRepositoryAdapter implements CombateRepositoryPort {

    private final CombateJpaRepository jpaRepository;
    private final EspecieJpaRepository especieJpaRepository;

    public CombateRepositoryAdapter(CombateJpaRepository jpaRepository,
                                     EspecieJpaRepository especieJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.especieJpaRepository = especieJpaRepository;
    }

    @Override
    @Transactional
    public Combate save(Combate combate) {
        CombateEntity entity = toEntity(combate);
        CombateEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Combate> findAll() {
        return jpaRepository.findAllByOrderByFechaDesc().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Combate toDomain(CombateEntity entity) {
        Combate combate = new Combate();
        combate.setId(entity.getId());
        combate.setEspecie1(EspecieMapper.toDomain(entity.getEspecie1()));
        combate.setEspecie2(EspecieMapper.toDomain(entity.getEspecie2()));
        combate.setGanador(EspecieMapper.toDomain(entity.getGanador()));
        combate.setNivelEfectivoEspecie1(entity.getNivelEfectivoEspecie1());
        combate.setNivelEfectivoEspecie2(entity.getNivelEfectivoEspecie2());
        combate.setModificadorEspecie1(entity.getModificadorEspecie1());
        combate.setModificadorEspecie2(entity.getModificadorEspecie2());
        combate.setFecha(entity.getFecha());
        return combate;
    }

    private CombateEntity toEntity(Combate combate) {
        if (combate.getEspecie1() == null || combate.getEspecie1().getId() == null) {
            throw new IllegalStateException("Especie1 no puede ser null o no persistida");
        }
        if (combate.getEspecie2() == null || combate.getEspecie2().getId() == null) {
            throw new IllegalStateException("Especie2 no puede ser null o no persistida");
        }
        if (combate.getGanador() == null || combate.getGanador().getId() == null) {
            throw new IllegalStateException("Ganador no puede ser null o no persistido");
        }

        EspecieEntity especie1Entity = especieJpaRepository
                .findById(combate.getEspecie1().getId())
                .orElseThrow(() -> new IllegalStateException(
                        "EspecieEntity con id " + combate.getEspecie1().getId() + " no encontrada"));
        EspecieEntity especie2Entity = especieJpaRepository
                .findById(combate.getEspecie2().getId())
                .orElseThrow(() -> new IllegalStateException(
                        "EspecieEntity con id " + combate.getEspecie2().getId() + " no encontrada"));
        EspecieEntity ganadorEntity = especieJpaRepository
                .findById(combate.getGanador().getId())
                .orElseThrow(() -> new IllegalStateException(
                        "EspecieEntity con id " + combate.getGanador().getId() + " no encontrada"));

        CombateEntity entity = new CombateEntity(
                especie1Entity,
                especie2Entity,
                ganadorEntity,
                combate.getNivelEfectivoEspecie1(),
                combate.getNivelEfectivoEspecie2(),
                combate.getModificadorEspecie1(),
                combate.getModificadorEspecie2(),
                combate.getFecha()
        );
        entity.setId(combate.getId());
        return entity;
    }
}
