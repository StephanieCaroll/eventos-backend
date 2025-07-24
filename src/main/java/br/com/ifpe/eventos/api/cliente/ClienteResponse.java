package br.com.ifpe.eventos.api.cliente;

import java.time.LocalDate;
import java.util.List;

import br.com.ifpe.eventos.modelo.evento.Evento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String foneCelular;
    private String email;
    private List<EventoSummary> eventosFavoritos;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventoSummary {
        private Long id;
        private String nomeEvento;
        private String descricao;
        private LocalDate dataInicio;
        private LocalDate dataFim;
        private String categoria;
        private String urlImagem;
        
        public static EventoSummary fromEvento(Evento evento) {
            return EventoSummary.builder()
                .id(evento.getId())
                .nomeEvento(evento.getNomeEvento())
                .descricao(evento.getDescricao())
                .dataInicio(evento.getDataInicio())
                .dataFim(evento.getDataFim())
                .categoria(evento.getCategoria())
                .urlImagem(evento.getUrlImagem())
                .build();
        }
    }
}