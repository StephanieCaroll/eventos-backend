package br.com.ifpe.eventos.api.stand;

import br.com.ifpe.eventos.modelo.stand.Stand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // gera get e set simultâneamente
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandRequest {
    private String codigo;
    private String userId; // ID do usuário que está cadastrando/selecionando o stand
    private Long eventoId; // ID do evento (opcional, para quando o stand é associado a um evento)

     public Stand build() {

       Stand e = Stand.builder() // tem a função de criar objetos com os seguintes atributos
           .codigo(codigo)
           .build();

           return e;
   }
}
