package br.com.ifpe.eventos.api.evento;

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
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.modelo.evento.EventoService;
import br.com.ifpe.eventos.modelo.stand.Stand;
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
            Evento evento = eventoService.findById(id);
            if (evento == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Buscar stands vinculados ao evento
            List<Stand> standsDoEvento = eventoService.getStandsDoEvento(id);
            List<StandSelectionDTO> standsDTO = new ArrayList<>();
            
            for (Stand stand : standsDoEvento) {
                StandSelectionDTO dto = StandSelectionDTO.builder()
                    .id(stand.getId())
                    .codigo(stand.getCodigo())
                    .descricao(stand.getDescricao())
                    .disponivel(stand.getUsuario() == null)
                    .selecionado(false)
                    .nomeEvento(evento.getNomeEvento())
                    .eventoId(evento.getId())
                    .emailUsuario(stand.getUsuario() != null ? stand.getUsuario().getUsername() : null)
                    .build();
                standsDTO.add(dto);
            }
            
            return ResponseEntity.ok(standsDTO);
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands disponíveis para evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    // Endpoint para obter stands não vinculados (disponíveis para cadastro em eventos)
    @GetMapping("/stands-sem-vinculo")
    public ResponseEntity<List<StandSelectionDTO>> getStandsSemVinculo() {
        try {
            // Buscar stands sem evento vinculado
            List<Stand> standsSemEvento = eventoService.getStandsDoEvento(null); // Busca stands sem evento
            List<StandSelectionDTO> standsDTO = new ArrayList<>();
            
            for (Stand stand : standsSemEvento) {
                StandSelectionDTO dto = StandSelectionDTO.builder()
                    .id(stand.getId())
                    .codigo(stand.getCodigo())
                    .descricao(stand.getDescricao())
                    .disponivel(true)
                    .selecionado(false)
                    .nomeEvento(null)
                    .eventoId(null)
                    .emailUsuario(null)
                    .build();
                standsDTO.add(dto);
            }
            
            return ResponseEntity.ok(standsDTO);
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands sem vínculo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    // Endpoint para vincular stands a um evento
    @PostMapping("/{id}/vincular-stands")
    public ResponseEntity<?> vincularStandsAoEvento(@PathVariable Long id, @RequestBody EventoStandRequest request) {
        try {
            // Validação
            if (request.getStandIds() == null || request.getStandIds().isEmpty()) {
                return ResponseEntity.badRequest().body("Lista de stands não pode estar vazia");
            }

            Evento evento = eventoService.findById(id);
            if (evento == null) {
                return ResponseEntity.notFound().build();
            }

            // Usar o método update para vincular os stands
            EventoRequest eventoRequest = EventoRequest.builder()
                .nomeEvento(evento.getNomeEvento())
                .descricao(evento.getDescricao())
                .dataInicio(evento.getDataInicio())
                .dataFim(evento.getDataFim())
                .horaInicio(evento.getHoraInicio())
                .horaFim(evento.getHoraFim())
                .categoria(evento.getCategoria())
                .organizador(evento.getOrganizador())
                .contatoOrganizador(evento.getContatoOrganizador())
                .urlImagem(evento.getUrlImagem())
                .tipoIngresso(evento.getTipoIngresso())
                .quantidadeIngressos(evento.getQuantidadeIngressos())
                .dataVendaInicio(evento.getDataVendaInicio())
                .dataVendaFim(evento.getDataVendaFim())
                .idsStands(request.getStandIds())
                .build();

            Evento eventoAtualizado = eventoService.update(id, eventoRequest);
            return ResponseEntity.ok(EventoResponse.fromEvento(eventoAtualizado));
        } catch (Exception e) {
            System.err.println("Erro ao vincular stands ao evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor: " + e.getMessage());
        }
    }
}