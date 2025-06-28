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
}