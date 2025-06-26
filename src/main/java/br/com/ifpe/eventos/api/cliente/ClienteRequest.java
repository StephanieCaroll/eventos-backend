package br.com.ifpe.eventos.api.cliente;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Arrays;

import br.com.ifpe.eventos.modelo.acesso.Perfil;
import br.com.ifpe.eventos.modelo.acesso.Usuario;
import br.com.ifpe.eventos.modelo.cliente.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotBlank(message = "O e-mail é de preenchimento obrigatório")
    @Email
    private String email;

    @NotBlank(message = "A senha é de preenchimento obrigatório")
    private String password;

    @NotBlank(message = "O nome é de preenchimento obrigatório")
    private String nome;

    @NotBlank(message = "A data de nascimento é de preenchimento obrigatório")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @NotBlank(message = "O telefone celular é de preenchimento obrigatório")
    private String foneCelular;

     public Usuario buildUsuario() {
       return Usuario.builder()
           .username(email)
           .password(password)
           .roles(Arrays.asList(new Perfil(Perfil.ROLE_CLIENTE)))
           .build();
   }


    public Cliente build() {

        return Cliente.builder()
                
                .usuario(buildUsuario())
                .nome(nome)
                .dataNascimento(dataNascimento)
                .foneCelular(foneCelular)
                .build();
    }

}
