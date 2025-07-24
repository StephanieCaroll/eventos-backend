package br.com.ifpe.eventos.modelo.stand.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StandBatchRequestDTO {
    
    @JsonProperty("userId")
    @JsonAlias({"usuarioId"})
    private String userId;
    
    @JsonProperty("eventoId")
    private Long eventoId;
    
    @JsonProperty("standIds")
    private List<Long> standIds;
    
    @JsonProperty("descricaoReserva")
    @JsonAlias({"observacoes"})
    private String descricaoReserva;
    
    @JsonProperty("tipoOperacao")
    @JsonAlias({"operacao"})
    private String tipoOperacao;
    
    // Converter para StandReservaDTO
    public StandReservaDTO toStandReservaDTO() {
        return StandReservaDTO.builder()
            .userId(this.userId)
            .eventoId(this.eventoId)
            .standIds(this.standIds)
            .descricaoReserva(this.descricaoReserva)
            .tipoOperacao(this.tipoOperacao)
            .build();
    }
}