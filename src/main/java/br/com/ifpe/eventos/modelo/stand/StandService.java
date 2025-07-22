package br.com.ifpe.eventos.modelo.stand;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.eventos.api.stand.StandRequest;
import br.com.ifpe.eventos.modelo.acesso.Usuario;
import br.com.ifpe.eventos.modelo.acesso.UsuarioService;
import br.com.ifpe.eventos.modelo.evento.Evento;
import br.com.ifpe.eventos.modelo.evento.EventoRepository;
import jakarta.transaction.Transactional;

@Service
public class StandService {

    @Autowired
    private StandRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EventoRepository eventoRepository;

    @Transactional
    public Stand save(StandRequest request) {
        Stand stand = request.build();
        
        // Associar usuário se fornecido
        if (request.getUserId() != null) {
            try {
                Usuario usuario = usuarioService.findByUsername(request.getUserId());
                stand.setUsuario(usuario);
            } catch (Exception e) {
                throw new RuntimeException("Usuário não encontrado: " + request.getUserId());
            }
        }
        
        // Associar evento se fornecido
        if (request.getEventoId() != null) {
            Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado."));
            stand.setEvento(evento);
        }
        
        stand.setHabilitado(Boolean.TRUE);
        return repository.save(stand);
    }

    @Transactional
    public Stand save(Stand stand) {
      
        stand.setHabilitado(Boolean.TRUE);
        return repository.save(stand);
    }

    public List<Stand> listarTodos() {
        return repository.findAll();
    }

    public List<Stand> listarStandsDisponiveis() {
     
        return repository.findByEventoIsNull();
    }

    public List<Stand> findAllById(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public List<Stand> getRegisteredStandsByUserId(String username) {
        try {
            List<Stand> stands = repository.findByUsuarioUsername(username);
            return stands != null ? stands : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro ao buscar stands do usuário " + username + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Stand> findByUsuarioUsernameAndEventoId(String username, Long eventId) {
        return repository.findByUsuarioUsernameAndEventoId(username, eventId);
    }

    public Stand obterPorID(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Stand não encontrado ou desabilitado."));
    }

    @Transactional
    public void update(Long id, Stand standAlterado) {
      
        Stand stand = repository.findById(id).orElseThrow(() -> new RuntimeException("Stand não encontrado para atualização."));

        stand.setCodigo(standAlterado.getCodigo());
       
        repository.save(stand); 
    }

    @Transactional
    public void delete(Long id) {
        Stand stand = repository.findById(id).orElseThrow(() -> new RuntimeException("Stand não encontrado para exclusão."));
        stand.setHabilitado(Boolean.FALSE); // Realiza soft delete
        repository.save(stand);
    }
}