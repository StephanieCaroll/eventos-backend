package br.com.ifpe.eventos.api.dono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping; 
import br.com.ifpe.eventos.modelo.dono.Dono;
import br.com.ifpe.eventos.modelo.dono.DonoService;

@RestController
@RequestMapping("/api/dono")
@CrossOrigin(origins = "http://localhost:3000")
public class DonoController {

    @Autowired
    private DonoService donoService;

    @PostMapping
    public ResponseEntity<Dono> save(@RequestBody DonoRequest request) {
        Dono dono = donoService.save(request.build());
        return new ResponseEntity<Dono>(dono, HttpStatus.CREATED);
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<Dono> getDonoByEmail(@PathVariable String email) {
        Dono dono = donoService.findByEmail(email);

        if (dono != null) {
            return ResponseEntity.ok(dono);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}") 
    public ResponseEntity<Dono> update(@PathVariable Long id, @RequestBody DonoRequest request) {
        try {
           
            Dono donoParaAtualizar = request.build();
            donoParaAtualizar.setId(id); 

            Dono donoAtualizado = donoService.update(donoParaAtualizar);

            if (donoAtualizado != null) {
                // Se a atualização for bem-sucedida, retorna 200 OK com o objeto atualizado
                return ResponseEntity.ok(donoAtualizado);
            } else {
                // Se o serviço retornar null, significa que o Dono com o ID não foi encontrado
                return ResponseEntity.notFound().build(); 
            }
        } catch (Exception e) {
            // Captura qualquer exceção que possa ocorrer durante o processo
            // e retorna 500 Internal Server Error, imprimindo o stack trace para depuração.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}