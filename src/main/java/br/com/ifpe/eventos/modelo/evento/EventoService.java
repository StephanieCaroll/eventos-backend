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
        evento.setHabilitado(Boolean.TRUE);

        // Primeiro salvar o evento
        Evento eventoSalvo = repository.save(evento);

        // Depois vincular os stands se fornecidos
        if (request.getIdsStands() != null && !request.getIdsStands().isEmpty()) {
            List<Stand> stands = standRepository.findAllById(request.getIdsStands());
            
            // Vincular cada stand ao evento
            for (Stand stand : stands) {
                stand.setEvento(eventoSalvo);
                standRepository.save(stand);
            }
            
            eventoSalvo.setStands(stands);
            eventoSalvo = repository.save(eventoSalvo);
        }

        return eventoSalvo;
    }

    public Evento findById(Long id) {
        Evento evento = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Evento com ID " + id + " não encontrado."));

        // Força a inicialização da coleção
        if (evento.getStands() != null) {
            evento.getStands().size(); 
        }
        return evento;
    }

    public List<Evento> findAll() {
        List<Evento> eventos = repository.findAll();

        eventos.forEach(evento -> {
            if (evento.getStands() != null) {
                evento.getStands().size(); 
            }
        });
        return eventos;
    }

    @Transactional
    public Evento update(Long id, EventoRequest request) {
        Optional<Evento> optionalEvento = repository.findById(id);

        if (optionalEvento.isEmpty()) {
            throw new EntityNotFoundException("Evento com ID " + id + " não encontrado.");
        }

        Evento eventoExistente = optionalEvento.get();

        // Atualizar campos básicos do evento
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

        // Gerenciar vinculação de stands
        if (request.getIdsStands() != null && !request.getIdsStands().isEmpty()) {
            // Remover vinculação de stands anteriores
            List<Stand> standsAntigos = standRepository.findByEventoId(id);
            for (Stand standAntigo : standsAntigos) {
                if (!request.getIdsStands().contains(standAntigo.getId())) {
                    standAntigo.setEvento(null);
                    standRepository.save(standAntigo);
                }
            }
            
            // Vincular novos stands
            List<Stand> novosStands = standRepository.findAllById(request.getIdsStands());
            for (Stand stand : novosStands) {
                stand.setEvento(eventoExistente);
                standRepository.save(stand);
            }
            
            eventoExistente.setStands(novosStands);
        } else {
            // Remover todos os stands vinculados
            List<Stand> standsAntigos = standRepository.findByEventoId(id);
            for (Stand standAntigo : standsAntigos) {
                standAntigo.setEvento(null);
                standRepository.save(standAntigo);
            }
            eventoExistente.setStands(Collections.emptyList());
        }

        Evento eventoAtualizado = repository.save(eventoExistente);
        return eventoAtualizado;
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Método para obter stands disponíveis para um evento específico
    public List<Stand> getStandsDisponiveisParaEvento(Long eventoId) {
        return standRepository.findStandsDisponiveisParaEvento(eventoId);
    }

    // Método para obter todos os stands vinculados a um evento
    public List<Stand> getStandsDoEvento(Long eventoId) {
        if (eventoId == null) {
            // Retorna stands sem evento vinculado (para cadastro)
            return standRepository.findByEventoIsNull();
        }
        return standRepository.findByEventoId(eventoId);
    }
}