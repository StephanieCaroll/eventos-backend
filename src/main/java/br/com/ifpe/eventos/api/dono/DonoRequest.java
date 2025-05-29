package br.com.ifpe.eventos.api.dono;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ifpe.eventos.modelo.dono.Dono;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonoRequest {
    
private String razaoSocial; //nome da empresa

private String nome;

private String email;

private String cpf;

@JsonFormat(pattern = "dd/MM/yyyy")
private LocalDate dataNascimento;

private String senha;

private String foneCelular;

public Dono build() {

        return Dono.builder()
            .razaoSocial(razaoSocial)
            .nome(nome)
            .email(email)
            .cpf(cpf)
            .dataNascimento(dataNascimento)
            .senha(senha)
            .foneCelular(foneCelular)
            .build();
    }

}
