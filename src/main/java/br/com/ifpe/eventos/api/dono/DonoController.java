package br.com.ifpe.eventos.api.dono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.dono.Dono;
import br.com.ifpe.eventos.modelo.dono.DonoService;

@RestController
@RequestMapping("/api/dono")
@CrossOrigin
public class DonoController {

   @Autowired
   private DonoService donoService;

   @PostMapping
   public ResponseEntity<Dono> save(@RequestBody DonoRequest request) {

       Dono dono = donoService.save(request.build());
       return new ResponseEntity <Dono> (dono, HttpStatus.CREATED);
   }
}
