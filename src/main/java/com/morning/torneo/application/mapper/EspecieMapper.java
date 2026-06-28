package com.morning.torneo.application.mapper;

import com.morning.torneo.application.dto.EspecieRequest;
import com.morning.torneo.application.dto.EspecieResponse;
import com.morning.torneo.domain.model.Especie;
import java.util.List;
import java.util.stream.Collectors;

public final class EspecieMapper {

    private EspecieMapper() {
    }

    public static Especie toDomain(EspecieRequest request) {
        Especie especie = new Especie();
        especie.setNombre(request.getNombre());
        especie.setNivelPoder(request.getNivelPoder());
        especie.setHabilidadEspecial(request.getHabilidadEspecial());
        return especie;
    }

    public static EspecieResponse toResponse(Especie especie) {
        EspecieResponse response = new EspecieResponse();
        response.setId(especie.getId());
        response.setNombre(especie.getNombre());
        response.setNivelPoder(especie.getNivelPoder());
        response.setHabilidadEspecial(especie.getHabilidadEspecial());
        response.setFechaCreacion(especie.getFechaCreacion());
        return response;
    }

    public static List<EspecieResponse> toResponseList(List<Especie> especies) {
        return especies.stream()
                .map(EspecieMapper::toResponse)
                .collect(Collectors.toList());
    }
}
