package br.com.ifpe.eventos.api.cliente;

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

import br.com.ifpe.eventos.modelo.cliente.Cliente;
import br.com.ifpe.eventos.modelo.cliente.ClienteService;
import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.modelo.evento.EventoService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:3000")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EventoService eventoService;

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

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody ClienteRequest request) {
        try {
            Cliente clienteExistente = clienteService.findById(id);
            if (clienteExistente == null) {
                return ResponseEntity.notFound().build();
            }

            clienteExistente.setNome(request.getNome());
            clienteExistente.setFoneCelular(request.getFoneCelular());
            clienteExistente.setDataNascimento(request.getDataNascimento());

            Cliente clienteAtualizado = clienteService.update(clienteExistente);

            return ResponseEntity.ok(clienteAtualizado);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar cliente com ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{email}/favoritar/{eventoId}")
    public ResponseEntity<?> favoritarEvento(@PathVariable String email, @PathVariable Long eventoId) {
        try {
            Cliente cliente = clienteService.findByUserEmail(email);
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
            }

            Evento evento = eventoService.findById(eventoId);
            if (evento == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado.");
            }

            clienteService.adicionarEventoFavorito(cliente, evento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println(
                    "Erro ao favoritar evento para " + email + " evento ID " + eventoId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao favoritar evento: " + e.getMessage());
        }
    }

    @DeleteMapping("/{email}/desfavoritar/{eventoId}")
    public ResponseEntity<?> desfavoritarEvento(@PathVariable String email, @PathVariable Long eventoId) {
        try {
            Cliente cliente = clienteService.findByUserEmail(email);
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
            }

            Evento evento = eventoService.findById(eventoId);
            if (evento == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado.");
            }

            clienteService.removerEventoFavorito(cliente, evento);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println(
                    "Erro ao desfavoritar evento para " + email + " evento ID " + eventoId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao desfavoritar evento: " + e.getMessage());
        }
    }

    @GetMapping("/favorited-events")
    public ResponseEntity<List<Evento>> getFavoritedEventsByEmail(@RequestParam String email) {
        try {
            Cliente cliente = clienteService.findByUserEmail(email);
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<Evento> favoritos = clienteService.getEventosFavoritos(cliente);
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            System.err.println("Erro ao buscar eventos favoritos para " + email + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}