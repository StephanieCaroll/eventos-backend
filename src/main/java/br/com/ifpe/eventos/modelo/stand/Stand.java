package br.com.ifpe.eventos.modelo.stand;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.eventos.modelo.acesso.Usuario;
import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Stand")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stand extends EntidadeAuditavel {
    
   @Column
    private String codigo;
    
    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}