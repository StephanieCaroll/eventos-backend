package br.com.ifpe.eventos.api.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.cliente.Cliente;
import br.com.ifpe.eventos.modelo.cliente.ClienteService;

@RestController
@RequestMapping("/api/clientes") 
@CrossOrigin(origins = "http://localhost:3000") 
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody ClienteRequest request) { 
        Cliente cliente = clienteService.save(request.build());
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @GetMapping("/by-email/{email}") 
    public ResponseEntity<Cliente> getClienteByEmail(@PathVariable String email) {
        Cliente cliente = clienteService.findByUserEmail(email);

        if (cliente == null) {
            
            return ResponseEntity.notFound().build(); 
        }
        return ResponseEntity.ok(cliente); 
    }
}