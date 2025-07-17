package br.com.ifpe.eventos.modelo.evento;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.eventos.api.evento.EventoRequest;
import br.com.ifpe.eventos.modelo.stand.Stand;
import br.com.ifpe.eventos.modelo.stand.StandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository repository;

    @Autowired
    private StandRepository standRepository;

    @Transactional
    public Evento salvar(EventoRequest request) {
        Evento evento = request.buildEvento();

        if (request.getIdsStands() != null && !request.getIdsStands().isEmpty()) {
            List<Stand> stands = standRepository.findAllById(request.getIdsStands());
            evento.setStands(stands);
        } else {
            evento.setStands(Collections.emptyList());
        }

        Evento eventoSalvo = repository.save(evento);
        // Inicializa a coleção de stands para garantir que seja carregada antes de retornar
        // Se ela já foi definida acima, este peek apenas garante o carregamento se a lógica de cima falhar por algum motivo
        eventoSalvo.getStands().size(); 
        return eventoSalvo;
    }

    public Evento findById(Long id) {
        Evento evento = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Evento com ID " + id + " não encontrado."));

        evento.getStands().size(); 
        return evento;
    }

    public List<Evento> findAll() {
        List<Evento> eventos = repository.findAll();

        eventos.forEach(evento -> evento.getStands().size()); 
        return eventos;
    }

    @Transactional
    public Evento update(Long id, EventoRequest request) {
        Optional<Evento> optionalEvento = repository.findById(id);

        if (optionalEvento.isEmpty()) {
            throw new EntityNotFoundException("Evento com ID " + id + " não encontrado.");
        }

        Evento eventoExistente = optionalEvento.get();

        eventoExistente.setNomeEvento(request.getNomeEvento());
        eventoExistente.setDescricao(request.getDescricao());
        eventoExistente.setDataInicio(request.getDataInicio());
        eventoExistente.setDataFim(request.getDataFim());
        eventoExistente.setHoraInicio(request.getHoraInicio());
        eventoExistente.setHoraFim(request.getHoraFim());
        eventoExistente.setCategoria(request.getCategoria());
        eventoExistente.setOrganizador(request.getOrganizador());
        eventoExistente.setContatoOrganizador(request.getContatoOrganizador());
        eventoExistente.setUrlImagem(request.getUrlImagem());
        eventoExistente.setTipoIngresso(request.getTipoIngresso());
        eventoExistente.setQuantidadeIngressos(request.getQuantidadeIngressos());
        eventoExistente.setDataVendaInicio(request.getDataVendaInicio());
        eventoExistente.setDataVendaFim(request.getDataVendaFim());

        if (request.getIdsStands() != null && !request.getIdsStands().isEmpty()) {
            List<Stand> novosStands = standRepository.findAllById(request.getIdsStands());
            eventoExistente.setStands(novosStands);
        } else {
            eventoExistente.setStands(Collections.emptyList());
        }

        Evento eventoAtualizado = repository.save(eventoExistente);
    
        eventoAtualizado.getStands().size();
        return eventoAtualizado;
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}