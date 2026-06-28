package com.morning.torneo.infrastructure.persistence.mapper;

import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.infrastructure.persistence.entity.EspecieEntity;

public final class EspecieMapper {

    private EspecieMapper() {
    }

    public static Especie toDomain(EspecieEntity entity) {
        Especie especie = new Especie();
        especie.setId(entity.getId());
        especie.setNombre(entity.getNombre());
        especie.setNivelPoder(entity.getNivelPoder());
        especie.setHabilidadEspecial(entity.getHabilidadEspecial());
        especie.setFechaCreacion(entity.getFechaCreacion());
        return especie;
    }

    public static EspecieEntity toEntity(Especie especie) {
        EspecieEntity entity = new EspecieEntity(
                especie.getNombre(),
                especie.getNivelPoder(),
                especie.getHabilidadEspecial(),
                especie.getFechaCreacion()
        );
        if (especie.getId() != null) {
            entity.setId(especie.getId());
        }
        return entity;
    }
}
