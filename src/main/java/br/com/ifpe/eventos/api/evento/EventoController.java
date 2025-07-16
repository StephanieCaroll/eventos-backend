package br.com.ifpe.eventos.api.evento;

import java.util.List;
import java.util.stream.Collectors;

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

import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.modelo.evento.EventoService;
import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.modelo.stand.StandService;

@RestController
@RequestMapping("/api/evento")
@CrossOrigin
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private StandService standService; // Injetar StandService

    @PostMapping
    public ResponseEntity<Evento> save(@RequestBody EventoRequest request) {
        Evento evento = request.buildEvento();

        // Se IDs de stands forem fornecidos, busque os stands e associe-os ao evento
        if (request.getIdsStands() != null && !request.getIdsStands().isEmpty()) {
            List<Stand> stands = request.getIdsStands().stream()
                                    .map(id -> standService.obterPorID(id)) // Assumindo que obterPorID retorna Stand
                                    .collect(Collectors.toList());
            evento.setStands(stands);
        }

        Evento eventoSalvo = eventoService.salvar(evento);
        return new ResponseEntity<>(eventoSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Evento> findAll() {
        return eventoService.findAll();
    }

    @GetMapping("/{id}")
    public Evento findById(@PathVariable Long id) {
        return eventoService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> update(@PathVariable("id") Long id, @RequestBody EventoRequest request) {
        Evento eventoAlterado = request.buildEvento();

        // Se IDs de stands forem fornecidos, busque os stands e associe-os ao evento
        if (request.getIdsStands() != null && !request.getIdsStands().isEmpty()) {
            List<Stand> stands = request.getIdsStands().stream()
                                    .map(standService::obterPorID)
                                    .collect(Collectors.toList());
            eventoAlterado.setStands(stands);
        } else {
            eventoAlterado.setStands(null); // Se nenhum stand for enviado, remove as associações existentes.
        }

        Evento eventoAtualizado = eventoService.update(id, eventoAlterado);
        return ResponseEntity.ok(eventoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventoService.delete(id);
        return ResponseEntity.ok().build();
    }
}