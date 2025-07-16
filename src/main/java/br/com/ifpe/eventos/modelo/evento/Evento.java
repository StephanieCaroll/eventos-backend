package br.com.ifpe.eventos.modelo.evento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    @JoinTable(
        name = "evento_stand",
        joinColumns = @JoinColumn(name = "evento_id"),
        inverseJoinColumns = @JoinColumn(name = "stand_id")
    )
    private List<Stand> stands;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(getId(), evento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}