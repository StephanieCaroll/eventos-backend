package br.com.ifpe.eventos.api.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.administrador.Adm;
import br.com.ifpe.eventos.modelo.administrador.AdmService;

@RestController
@RequestMapping("/api/administrador")
@CrossOrigin
public class AdmController {

    @Autowired
    private AdmService admService;

    @PostMapping
    public ResponseEntity<Adm> save(@RequestBody AdmRequest request) {
        Adm adm = admService.save(request.build());
        return new ResponseEntity<Adm>(adm, HttpStatus.CREATED);
    }
}
