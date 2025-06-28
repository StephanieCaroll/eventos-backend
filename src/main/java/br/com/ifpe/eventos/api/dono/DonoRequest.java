package br.com.ifpe.eventos.api.dono;

import java.time.LocalDate;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ifpe.eventos.modelo.acesso.Perfil;
import br.com.ifpe.eventos.modelo.acesso.Usuario;
import br.com.ifpe.eventos.modelo.dono.Dono;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "O e-mail é de preenchimento obrigatório")
    @Email
    private String email;

    @NotBlank(message = "A senha é de preenchimento obrigatório")
    private String password;

    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    private String foneCelular;

    public Usuario buildUsuario() {
        return Usuario.builder()
            .username(email)
            .password(password)
            .roles(Arrays.asList(new Perfil(Perfil.ROLE_GERENCIADOR)))
            .build();
    }

    public Dono build() {
        return Dono.builder()
            .razaoSocial(razaoSocial)
            .nome(nome)
            .cpf(cpf)
            .dataNascimento(dataNascimento)
            .foneCelular(foneCelular)
            .usuario(buildUsuario())
            .build();
    }
}