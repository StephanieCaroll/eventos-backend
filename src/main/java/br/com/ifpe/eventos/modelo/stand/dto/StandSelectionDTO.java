package br.com.ifpe.eventos.modelo.stand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandSelectionDTO {
    private Long id;
    private String codigo;
    private String descricao;
    private boolean disponivel;
    private boolean selecionado;
    private String nomeEvento;
    private Long eventoId;
    private String nomeUsuario;
    private String emailUsuario;
}