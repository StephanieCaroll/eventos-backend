package br.com.ifpe.eventos.modelo.cliente;

import java.time.LocalDate;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.eventos.modelo.acesso.Usuario;
import br.com.ifpe.eventos.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Cliente")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends EntidadeAuditavel {

    @OneToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @Column
    private String nome;

    // @Column
    // private String email;

    @Column
    private LocalDate dataNascimento;

    @Column
    private String foneCelular;

    // @Column
    // private String senha;

}
