package br.com.ifpe.eventos.api.evento;

import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.modelo.evento.EventoRepository; 
import br.com.ifpe.eventos.modelo.evento.EventoService;

@RestController
@RequestMapping("/api/evento")
@CrossOrigin 
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired 
    private EventoRepository eventoRepository;

    @PostMapping
    public ResponseEntity<Evento> save(@RequestBody EventoRequest request) {
        Evento evento = eventoService.salvar(request.build());
        return new ResponseEntity<>(evento, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Evento>> findAll() {
      
        List<Evento> eventos = eventoService.findAll(); 
        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    @PutMapping("/{id}") 
    public ResponseEntity<Evento> update(@PathVariable Long id, @RequestBody EventoRequest request) {

        return eventoRepository.findById(id)
            .map(existingEvento -> {
                existingEvento.setNomeEvento(request.getNomeEvento());
                existingEvento.setDescricao(request.getDescricao());
                existingEvento.setDataInicio(request.getDataInicio());
                existingEvento.setDataFim(request.getDataFim());
                existingEvento.setHoraInicio(request.getHoraInicio());
                existingEvento.setHoraFim(request.getHoraFim());
                existingEvento.setCategoria(request.getCategoria());
                existingEvento.setOrganizador(request.getOrganizador());
                existingEvento.setContatoOrganizador(request.getContatoOrganizador());
                existingEvento.setUrlImagem(request.getUrlImagem());
                existingEvento.setTipoIngresso(request.getTipoIngresso());
                existingEvento.setQuantidadeIngressos(request.getQuantidadeIngressos());
                existingEvento.setDataVendaInicio(request.getDataVendaInicio());
                existingEvento.setDataVendaFim(request.getDataVendaFim());

                Evento updatedEvento = eventoService.salvar(existingEvento);
                return new ResponseEntity<>(updatedEvento, HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return eventoRepository.findById(id)
            .map(evento -> {
                evento.setHabilitado(Boolean.FALSE); 
                eventoService.salvar(evento); 
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT); 
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}