package br.com.ifpe.eventos.modelo.stand.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandReservaDTO {
    private String userId;
    private Long eventoId;
    private List<Long> standIds;
    private String descricaoReserva;
    private String tipoOperacao; // "RESERVAR" ou "LIBERAR"
}