package br.com.ifpe.eventos.api.stand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.modelo.stand.StandService;

@RestController //determina a classe como controller
@RequestMapping("/api/stand") // especifica o endereço do controller
@CrossOrigin 
public class StandController {
     @Autowired //vai instanciar objetos do tipo ClienteService e colocar dentro da variável, para que ela seja utilizada em  todas funções do controller 
   private StandService standService;

   @PostMapping//especifica que essa função vai receber requisição post
   public ResponseEntity <Stand> save(@RequestBody StandRequest request) {

       Stand stand = standService.save(request.build());
       return new ResponseEntity<Stand>(stand, HttpStatus.CREATED);
   }
   @GetMapping //vai retornar uma lista com todos os clientes
    public List<Stand> listarTodos() {

        return standService.listarTodos();
    }

  
    @GetMapping("/{id}") //retorna o cliente pesquisado pelo id
    public Stand obterPorID(@PathVariable Long id) {
        return standService.obterPorID(id);
    }

     @PutMapping("/{id}") //informar via url o id do produto | abaixo ele passa os dados do produto alterado no corpo da requisição
 public ResponseEntity <Stand> update(@PathVariable("id") Long id, @RequestBody StandRequest request) {
  //passando os dados para a função update
       standService.update(id, request.build());
       return ResponseEntity.ok().build();
 }
@DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {

       standService.delete(id);
       return ResponseEntity.ok().build();
   }
}
