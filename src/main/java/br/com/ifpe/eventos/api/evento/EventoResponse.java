package br.com.ifpe.eventos.api.evento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.modelo.stand.Stand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponse {
    private Long id;
    private String nomeEvento;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String categoria;
    private String organizador;
    private String contatoOrganizador;
    private String urlImagem;
    private String tipoIngresso;
    private Integer quantidadeIngressos;
    private LocalDate dataVendaInicio;
    private LocalDate dataVendaFim;
    private List<StandSummary> stands;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StandSummary {
        private Long id;
        private String codigo;
        private String descricao;
        private String emailUsuario;
        
        public static StandSummary fromStand(Stand stand) {
            return StandSummary.builder()
                .id(stand.getId())
                .codigo(stand.getCodigo())
                .descricao(stand.getDescricao())
                .emailUsuario(stand.getUsuario() != null ? stand.getUsuario().getUsername() : null)
                .build();
        }
    }
    
    public static EventoResponse fromEvento(Evento evento) {
        return EventoResponse.builder()
            .id(evento.getId())
            .nomeEvento(evento.getNomeEvento())
            .descricao(evento.getDescricao())
            .dataInicio(evento.getDataInicio())
            .dataFim(evento.getDataFim())
            .horaInicio(evento.getHoraInicio())
            .horaFim(evento.getHoraFim())
            .categoria(evento.getCategoria())
            .organizador(evento.getOrganizador())
            .contatoOrganizador(evento.getContatoOrganizador())
            .urlImagem(evento.getUrlImagem())
            .tipoIngresso(evento.getTipoIngresso())
            .quantidadeIngressos(evento.getQuantidadeIngressos())
            .dataVendaInicio(evento.getDataVendaInicio())
            .dataVendaFim(evento.getDataVendaFim())
            .stands(evento.getStands() != null ? 
                evento.getStands().stream()
                    .map(StandSummary::fromStand)
                    .collect(java.util.stream.Collectors.toList()) : 
                java.util.Collections.emptyList())
            .build();
    }
}