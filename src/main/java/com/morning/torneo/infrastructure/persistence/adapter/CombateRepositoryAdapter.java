package com.morning.torneo.infrastructure.persistence.adapter;

import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.infrastructure.persistence.entity.CombateEntity;
import com.morning.torneo.infrastructure.persistence.entity.EspecieEntity;
import com.morning.torneo.infrastructure.persistence.repository.CombateJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CombateRepositoryAdapter implements CombateRepositoryPort {

    private final CombateJpaRepository jpaRepository;

    public CombateRepositoryAdapter(CombateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Combate save(Combate combate) {
        CombateEntity entity = toEntity(combate);
        CombateEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Combate> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Combate toDomain(CombateEntity entity) {
        Combate combate = new Combate();
        combate.setId(entity.getId());
        combate.setEspecie1(toDomainEspecie(entity.getEspecie1()));
        combate.setEspecie2(toDomainEspecie(entity.getEspecie2()));
        combate.setGanador(toDomainEspecie(entity.getGanador()));
        combate.setNivelEfectivoEspecie1(entity.getNivelEfectivoEspecie1());
        combate.setNivelEfectivoEspecie2(entity.getNivelEfectivoEspecie2());
        combate.setModificadorEspecie1(entity.getModificadorEspecie1());
        combate.setModificadorEspecie2(entity.getModificadorEspecie2());
        combate.setFecha(entity.getFecha());
        return combate;
    }

    private CombateEntity toEntity(Combate combate) {
        CombateEntity entity = new CombateEntity(
                toEntityEspecie(combate.getEspecie1()),
                toEntityEspecie(combate.getEspecie2()),
                toEntityEspecie(combate.getGanador()),
                combate.getNivelEfectivoEspecie1(),
                combate.getNivelEfectivoEspecie2(),
                combate.getModificadorEspecie1(),
                combate.getModificadorEspecie2(),
                combate.getFecha()
        );
        entity.setId(combate.getId());
        return entity;
    }

    private Especie toDomainEspecie(EspecieEntity entity) {
        Especie especie = new Especie();
        especie.setId(entity.getId());
        especie.setNombre(entity.getNombre());
        especie.setNivelPoder(entity.getNivelPoder());
        especie.setHabilidadEspecial(entity.getHabilidadEspecial());
        especie.setFechaCreacion(entity.getFechaCreacion());
        return especie;
    }

    private EspecieEntity toEntityEspecie(Especie especie) {
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
