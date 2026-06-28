package com.morning.torneo.application.mapper;

import com.morning.torneo.application.dto.RankingResponse;
import com.morning.torneo.domain.model.EspecieRanking;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class RankingMapper {

    private RankingMapper() {
    }

    public static List<RankingResponse> toResponseList(List<EspecieRanking> ranking) {
        return IntStream.range(0, ranking.size())
                .mapToObj(i -> {
                    EspecieRanking item = ranking.get(i);
                    RankingResponse response = new RankingResponse();
                    response.setPosicion(i + 1);
                    response.setEspecie(EspecieMapper.toResponse(item.getEspecie()));
                    response.setVictorias(item.getVictorias());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
