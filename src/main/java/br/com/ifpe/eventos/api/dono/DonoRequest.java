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
    
    private String razaoSocial;

    private String nome;

private String email;

private String cpf;
    private String rg;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    private String enderecoRua;
    private String enderecoComplemento;
    private String enderecoNumero;
    private String enderecoBairro;
    private String enderecoCidade;
    private String enderecoCep;
    private String enderecoUf;

public Dono build() {

        return Dono.builder()
            .razaoSocial(razaoSocial)
            .nome(nome)
            .email(email)
            .cpf(cpf)
            .rg(rg)
            .dataNascimento(dataNascimento)
            .enderecoRua(enderecoRua)
            .enderecoComplemento(enderecoComplemento)
            .enderecoNumero(enderecoNumero)
            .enderecoBairro(enderecoBairro)
            .enderecoCidade(enderecoCidade)
            .enderecoCep(enderecoCep)
            .enderecoUf(enderecoUf)
            .build();
    }

}
