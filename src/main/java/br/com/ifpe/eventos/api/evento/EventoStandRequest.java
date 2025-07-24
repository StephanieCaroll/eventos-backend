package br.com.ifpe.eventos.api.evento;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoStandRequest {
    private Long eventoId;
    private List<Long> standIds;
}