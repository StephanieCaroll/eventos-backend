package br.com.ifpe.eventos.api.evento;

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

import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.modelo.evento.EventoService;
import br.com.ifpe.eventos.modelo.stand.dto.StandSelectionDTO;

@RestController
@RequestMapping("/api/evento")
@CrossOrigin
public class EventoController {

    @Autowired
    private EventoService eventoService;


    @PostMapping
    public ResponseEntity<EventoResponse> save(@RequestBody EventoRequest request) {
     
        Evento eventoSalvo = eventoService.salvar(request);
        return new ResponseEntity<>(EventoResponse.fromEvento(eventoSalvo), HttpStatus.CREATED);
    }

    @GetMapping
    public List<EventoResponse> findAll() {
        return eventoService.findAll().stream()
            .map(EventoResponse::fromEvento)
            .collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventoResponse findById(@PathVariable Long id) {
        return EventoResponse.fromEvento(eventoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> update(@PathVariable("id") Long id, @RequestBody EventoRequest request) {

        Evento eventoAtualizado = eventoService.update(id, request);
        return ResponseEntity.ok(EventoResponse.fromEvento(eventoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventoService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint para obter stands disponíveis para um evento (para facilitar seleção visual)
    @GetMapping("/{id}/stands-disponiveis")
    public ResponseEntity<List<StandSelectionDTO>> getStandsDisponiveisParaEvento(@PathVariable Long id) {
        try {
            // Este endpoint poderia usar o StandService diretamente
            // mas mantendo a responsabilidade no EventoService se necessário
            Evento evento = eventoService.findById(id);
            if (evento == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Por enquanto, retornamos uma lista vazia - a implementação real
            // deveria usar um método no EventoService que usa o StandService
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands disponíveis para evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
}