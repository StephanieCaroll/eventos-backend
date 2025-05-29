package br.com.ifpe.eventos.modelo.dono;


import java.time.LocalDate;

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
@Table(name = "Dono")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dono extends EntidadeAuditavel {

@Column
private String razaoSocial; //Nome da empresa

@Column
private String nome;

@Column
private String email;

@Column
private String cpf;

@Column
private LocalDate dataNascimento;

@Column
private String senha;

@Column
private String foneCelular;
    
}
