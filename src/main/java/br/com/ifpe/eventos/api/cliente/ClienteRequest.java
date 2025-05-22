package br.com.ifpe.eventos.api.cliente;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ifpe.eventos.modelo.cliente.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

   private String nome;

   private String email;

   @JsonFormat(pattern = "dd/MM/yyyy")
   private LocalDate dataNascimento;

   private String foneCelular;

   private String senha;

   public Cliente build() {

       return Cliente.builder()
           .nome(nome)
           .email(email)
           .dataNascimento(dataNascimento)
           .foneCelular(foneCelular)
           .senha(senha)
           .build();
   }

}
