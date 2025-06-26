package br.com.ifpe.eventos.api.evento;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    private LocalTime horaInicio;
    private LocalTime horaFim;

    private String categoria;
    private String organizador;
    private String contatoOrganizador;
    private String urlImagem;
    private String tipoIngresso;
    private Integer quantidadeIngressos;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVendaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVendaFim;

    public Evento build() {
        return Evento.builder()
            .nomeEvento(nomeEvento)
            .descricao(descricao)
            .dataInicio(dataInicio)
            .dataFim(dataFim)
            .horaInicio(horaInicio)
            .horaFim(horaFim)
            .categoria(categoria)
            .organizador(organizador)
            .contatoOrganizador(contatoOrganizador)
            .urlImagem(urlImagem)
            .tipoIngresso(tipoIngresso)
            .quantidadeIngressos(quantidadeIngressos)
            .dataVendaInicio(dataVendaInicio)
            .dataVendaFim(dataVendaFim)
            .build();
    }
}
