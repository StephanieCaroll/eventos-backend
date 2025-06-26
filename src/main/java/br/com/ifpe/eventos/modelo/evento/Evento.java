package br.com.ifpe.eventos.modelo.evento;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.eventos.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Evento")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evento extends EntidadeAuditavel {

    @Column
    private String nomeEvento;

    @Column
    private String descricao;

    @Column
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFim;

    @Column
    private LocalTime horaInicio;

    @Column
    private LocalTime horaFim;

    @Column
    private String categoria;

    @Column
    private String organizador;

    @Column
    private String contatoOrganizador;

    @Column
    private String urlImagem;

    @Column
    private String tipoIngresso;

    @Column
    private Integer quantidadeIngressos;

    @Column
    private LocalDate dataVendaInicio;

    @Column
    private LocalDate dataVendaFim;
}
