package com.morning.torneo.application.mapper;

import com.morning.torneo.application.dto.CombateRequest;
import com.morning.torneo.application.dto.CombateResponse;
import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.IniciarCombateCommand;
import java.util.List;
import java.util.stream.Collectors;

public final class CombateMapper {

    private CombateMapper() {
    }

    public static CombateResponse toResponse(Combate combate) {
        CombateResponse response = new CombateResponse();
        response.setId(combate.getId());
        response.setEspecie1(EspecieMapper.toResponse(combate.getEspecie1()));
        response.setEspecie2(EspecieMapper.toResponse(combate.getEspecie2()));
        if (combate.getGanador() != null) {
            response.setGanador(EspecieMapper.toResponse(combate.getGanador()));
        }
        response.setNivelEfectivoEspecie1(combate.getNivelEfectivoEspecie1());
        response.setNivelEfectivoEspecie2(combate.getNivelEfectivoEspecie2());
        response.setModificadorEspecie1(combate.getModificadorEspecie1());
        response.setModificadorEspecie2(combate.getModificadorEspecie2());
        response.setFecha(combate.getFecha());
        return response;
    }

    public static IniciarCombateCommand toCommand(CombateRequest request) {
        return new IniciarCombateCommand(
            request.getEspecie1Id(),
            request.getEspecie2Id(),
            request.isEsDesempate()
        );
    }

    public static List<CombateResponse> toResponseList(List<Combate> combates) {
        return combates.stream()
                .map(CombateMapper::toResponse)
                .collect(Collectors.toList());
    }
}
