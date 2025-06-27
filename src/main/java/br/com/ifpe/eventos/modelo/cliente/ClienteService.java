package br.com.ifpe.eventos.modelo.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.eventos.modelo.acesso.Perfil;
import br.com.ifpe.eventos.modelo.acesso.PerfilRepository;
import br.com.ifpe.eventos.modelo.acesso.Usuario;
import br.com.ifpe.eventos.modelo.acesso.UsuarioRepository; 
import br.com.ifpe.eventos.modelo.acesso.UsuarioService;
import jakarta.transaction.Transactional;
import java.util.Optional;

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

    // Este é o método que o seu controlador REST irá chamar.
    public Cliente findByUserEmail(String email) {
        // Primeiro, encontre o Usuario pelo seu username 
        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Se o Usuario for encontrado, então procure o Cliente que está associado a este Usuario
            return repository.findByUsuario(usuario).orElse(null); // Retorna o Cliente ou null se não encontrado
        }
        return null; // Retorna null se nenhum Usuario for encontrado para o email
    }
}