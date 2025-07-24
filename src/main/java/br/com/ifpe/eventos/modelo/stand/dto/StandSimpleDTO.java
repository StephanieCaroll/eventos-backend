package br.com.ifpe.eventos.modelo.stand.dto;

import br.com.ifpe.eventos.modelo.stand.Stand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandSimpleDTO {
    private Long id;
    private String codigo;
    private String descricao;
    private Long eventoId;
    private String nomeEvento;
    private String emailUsuario;

    public static StandSimpleDTO fromStand(Stand stand) {
        return StandSimpleDTO.builder()
            .id(stand.getId())
            .codigo(stand.getCodigo())
            .descricao(stand.getDescricao())
            .eventoId(stand.getEvento() != null ? stand.getEvento().getId() : null)
            .nomeEvento(stand.getEvento() != null ? stand.getEvento().getNomeEvento() : null)
            .emailUsuario(stand.getUsuario() != null ? stand.getUsuario().getUsername() : null)
            .build();
    }
}