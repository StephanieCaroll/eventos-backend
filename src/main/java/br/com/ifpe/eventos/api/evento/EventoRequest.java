package br.com.ifpe.eventos.api.evento;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class EventoRequest {
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
    private List<Long> idsStands;

    public Evento buildEvento() {
        return Evento.builder()
            .nomeEvento(this.nomeEvento)
            .descricao(this.descricao)
            .dataInicio(this.dataInicio)
            .dataFim(this.dataFim)
            .horaInicio(this.horaInicio)
            .horaFim(this.horaFim)
            .categoria(this.categoria)
            .organizador(this.organizador)
            .contatoOrganizador(this.contatoOrganizador)
            .urlImagem(this.urlImagem)
            .tipoIngresso(this.tipoIngresso)
            .quantidadeIngressos(this.quantidadeIngressos)
            .dataVendaInicio(this.dataVendaInicio)
            .dataVendaFim(this.dataVendaFim)
            .build();
    }
}