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
import br.com.ifpe.eventos.modelo.stand.dto.StandReservaDTO;
import br.com.ifpe.eventos.modelo.stand.dto.StandSelectionDTO;
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

    // Novo método para listar stands com informações de seleção
    public List<StandSelectionDTO> listarStandsParaSelecao(Long eventoId, String userId) {
        List<Stand> todosStands = repository.findAll();
        List<StandSelectionDTO> standsParaSelecao = new ArrayList<>();
        
        for (Stand stand : todosStands) {
            StandSelectionDTO dto = StandSelectionDTO.builder()
                .id(stand.getId())
                .codigo(stand.getCodigo())
                .descricao(stand.getDescricao())
                .disponivel(stand.getEvento() == null || (eventoId != null && stand.getEvento().getId().equals(eventoId)))
                .selecionado(userId != null && stand.getUsuario() != null && 
                           stand.getUsuario().getUsername().equals(userId))
                .nomeEvento(stand.getEvento() != null ? stand.getEvento().getNomeEvento() : null)
                .eventoId(stand.getEvento() != null ? stand.getEvento().getId() : null)
                .emailUsuario(stand.getUsuario() != null ? stand.getUsuario().getUsername() : null)
                .build();
            standsParaSelecao.add(dto);
        }
        
        return standsParaSelecao;
    }

    // Método para reservar/liberar múltiplos stands
    @Transactional
    public List<Stand> processarReservaStands(StandReservaDTO reservaDTO) {
        List<Stand> standsProcessados = new ArrayList<>();
        Usuario usuario = null;
        Evento evento = null;

        try {
            // Validação dos parâmetros de entrada
            if (reservaDTO.getStandIds() == null || reservaDTO.getStandIds().isEmpty()) {
                throw new RuntimeException("Lista de stands não pode estar vazia");
            }

            if (reservaDTO.getTipoOperacao() == null) {
                throw new RuntimeException("Tipo de operação é obrigatório");
            }

            // Buscar usuário se fornecido
            if (reservaDTO.getUserId() != null) {
                try {
                    usuario = usuarioService.findByUsername(reservaDTO.getUserId());
                    if (usuario == null) {
                        throw new RuntimeException("Usuário não encontrado: " + reservaDTO.getUserId());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao buscar usuário: " + reservaDTO.getUserId() + ". " + e.getMessage());
                }
            }

            // Buscar evento se fornecido
            if (reservaDTO.getEventoId() != null) {
                evento = eventoRepository.findById(reservaDTO.getEventoId())
                    .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + reservaDTO.getEventoId()));
            }

            // Processar cada stand
            for (Long standId : reservaDTO.getStandIds()) {
                try {
                    Stand stand = repository.findById(standId)
                        .orElseThrow(() -> new RuntimeException("Stand não encontrado com ID: " + standId));

                    if ("RESERVAR".equals(reservaDTO.getTipoOperacao())) {
                        // Verificar se stand está disponível
                        if (stand.getEvento() != null && !stand.getEvento().getId().equals(reservaDTO.getEventoId())) {
                            throw new RuntimeException("Stand " + stand.getCodigo() + " já está ocupado por outro evento.");
                        }
                        
                        stand.setUsuario(usuario);
                        stand.setEvento(evento);
                        if (reservaDTO.getDescricaoReserva() != null) {
                            stand.setDescricao(reservaDTO.getDescricaoReserva());
                        }
                    } else if ("LIBERAR".equals(reservaDTO.getTipoOperacao())) {
                        // Verificar se o usuário pode liberar este stand
                        if (stand.getUsuario() != null && !stand.getUsuario().getUsername().equals(reservaDTO.getUserId())) {
                            throw new RuntimeException("Usuário não tem permissão para liberar o stand " + stand.getCodigo());
                        }
                        
                        stand.setUsuario(null);
                        stand.setEvento(null);
                        stand.setDescricao("Stand " + stand.getCodigo() + " - Disponível para reserva");
                    } else {
                        throw new RuntimeException("Tipo de operação inválido: " + reservaDTO.getTipoOperacao() + ". Use 'RESERVAR' ou 'LIBERAR'.");
                    }

                    standsProcessados.add(repository.save(stand));
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao processar stand ID " + standId + ": " + e.getMessage());
                }
            }

            return standsProcessados;
        } catch (Exception e) {
            System.err.println("Erro em processarReservaStands: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar reserva de stands: " + e.getMessage());
        }
    }

    // Método para obter stands disponíveis para um evento específico
    public List<StandSelectionDTO> listarStandsDisponiveisParaEvento(Long eventoId) {
        List<Stand> stands = repository.findAll();
        List<StandSelectionDTO> standsDisponiveis = new ArrayList<>();
        
        for (Stand stand : stands) {
            if (stand.getEvento() == null || stand.getEvento().getId().equals(eventoId)) {
                StandSelectionDTO dto = StandSelectionDTO.builder()
                    .id(stand.getId())
                    .codigo(stand.getCodigo())
                    .descricao(stand.getDescricao())
                    .disponivel(true)
                    .selecionado(false)
                    .eventoId(eventoId)
                    .build();
                standsDisponiveis.add(dto);
            }
        }
        
        return standsDisponiveis;
    }

    // Método para obter stand por código
    public Stand findByCode(String codigo) {
        return repository.findAll().stream()
            .filter(stand -> stand.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);
    }

    // Método para verificar se um stand está disponível para um evento
    public boolean isStandDisponivel(Long standId, Long eventoId) {
        Stand stand = repository.findById(standId).orElse(null);
        if (stand == null) return false;
        
        return stand.getEvento() == null || 
               (eventoId != null && stand.getEvento().getId().equals(eventoId));
    }

    // Método para obter informações resumidas de um stand
    public StandSelectionDTO getStandInfo(Long standId, String userId) {
        Stand stand = repository.findById(standId).orElse(null);
        if (stand == null) return null;
        
        return StandSelectionDTO.builder()
            .id(stand.getId())
            .codigo(stand.getCodigo())
            .descricao(stand.getDescricao())
            .disponivel(stand.getEvento() == null)
            .selecionado(userId != null && stand.getUsuario() != null && 
                       stand.getUsuario().getUsername().equals(userId))
            .nomeEvento(stand.getEvento() != null ? stand.getEvento().getNomeEvento() : null)
            .eventoId(stand.getEvento() != null ? stand.getEvento().getId() : null)
            .emailUsuario(stand.getUsuario() != null ? stand.getUsuario().getUsername() : null)
            .build();
    }
}