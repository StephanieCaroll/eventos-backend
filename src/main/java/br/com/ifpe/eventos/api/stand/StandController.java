package br.com.ifpe.eventos.api.stand;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.modelo.stand.StandService;

@RestController
@RequestMapping("/api/stand") 
@CrossOrigin
public class StandController {

    @Autowired
    private StandService standService;

    @PostMapping
    public ResponseEntity<Stand> save(@RequestBody StandRequest request) {
        Stand stand = standService.save(request);
        return new ResponseEntity<>(stand, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Stand> listarTodos() {
        return standService.listarTodos();
    }

    @GetMapping("/disponiveis")
    public List<Stand> listarStandsDisponiveis(@RequestParam(required = false) Long eventId) {
      
        return standService.listarStandsDisponiveis(); 
    }

    @GetMapping("/usuario") 
    public ResponseEntity<List<Stand>> getStandsDoUsuarioPorEvento(
            @RequestParam String userId, 
            @RequestParam Long eventId) {
        try {
            List<Stand> stands = standService.findByUsuarioUsernameAndEventoId(userId, eventId);
            return new ResponseEntity<>(stands, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands do usuário por evento: " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @GetMapping("/registered")
    public ResponseEntity<List<Stand>> getRegisteredStandsByUserId(@RequestParam String userId) {
        try {
            List<Stand> stands = standService.getRegisteredStandsByUserId(userId);
            return new ResponseEntity<>(stands, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands registrados do usuário: " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public Stand obterPorID(@PathVariable Long id) {
        return standService.obterPorID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stand> update(@PathVariable("id") Long id, @RequestBody StandRequest request) {
        standService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        standService.delete(id);
        return ResponseEntity.ok().build();
    }
}