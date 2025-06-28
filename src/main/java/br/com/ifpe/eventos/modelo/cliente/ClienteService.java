package br.com.ifpe.eventos.modelo.cliente;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.eventos.modelo.acesso.Perfil;
import br.com.ifpe.eventos.modelo.acesso.PerfilRepository;
import br.com.ifpe.eventos.modelo.acesso.Usuario;
import br.com.ifpe.eventos.modelo.acesso.UsuarioRepository;
import br.com.ifpe.eventos.modelo.acesso.UsuarioService;
import br.com.ifpe.eventos.modelo.evento.Evento;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilRepository perfilUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Cliente save(Cliente cliente) {

        usuarioService.save(cliente.getUsuario());

        for (Perfil perfil : cliente.getUsuario().getRoles()) {
            perfil.setHabilitado(Boolean.TRUE);
            perfilUsuarioRepository.save(perfil);
        }

        cliente.setHabilitado(Boolean.TRUE);
        return repository.save(cliente);
    }

    public Cliente findByUserEmail(String email) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            return repository.findByUsuario(usuario).orElse(null);
        }
        return null;
    }

    public Cliente findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Cliente update(Cliente cliente) {
        return repository.save(cliente);
    }

    @Transactional
    public void adicionarEventoFavorito(Cliente cliente, Evento evento) {
        if (cliente.getEventosFavoritos() == null) {
            cliente.setEventosFavoritos(new java.util.ArrayList<>());
        }
        if (!cliente.getEventosFavoritos().contains(evento)) { 
            cliente.getEventosFavoritos().add(evento);
            repository.save(cliente); 
        }
    }

    @Transactional
    public void removerEventoFavorito(Cliente cliente, Evento evento) {
        if (cliente.getEventosFavoritos() != null) {
            cliente.getEventosFavoritos().remove(evento);
            repository.save(cliente); 
        }
    }

    public List<Evento> getEventosFavoritos(Cliente cliente) {
        if (cliente != null && cliente.getEventosFavoritos() != null) {
          
            cliente.getEventosFavoritos().size(); 
            return List.copyOf(cliente.getEventosFavoritos()); 
        }
        return Collections.emptyList();
    }
}
