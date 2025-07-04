package br.com.ifpe.eventos.modelo.dono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.eventos.modelo.acesso.Perfil;
import br.com.ifpe.eventos.modelo.acesso.PerfilRepository;
import br.com.ifpe.eventos.modelo.acesso.UsuarioService;
import jakarta.transaction.Transactional; 

@Service
public class DonoService {

    @Autowired
    private DonoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilRepository perfilUsuarioRepository;

    @Transactional
    public Dono save(Dono dono) {
        usuarioService.save(dono.getUsuario());

        for (Perfil perfil : dono.getUsuario().getRoles()) {
            perfil.setHabilitado(Boolean.TRUE);
            perfilUsuarioRepository.save(perfil);
        }

        dono.setHabilitado(Boolean.TRUE);
        return repository.save(dono);
    }

    public Dono findByEmail(String email) {
        return repository.findByUsuarioUsername(email).orElse(null);
    }

    @Transactional 
    public Dono update(Dono donoComNovosDados) {
    
        return repository.findById(donoComNovosDados.getId())
                .map(donoExistente -> {

                    donoExistente.setNome(donoComNovosDados.getNome());
                    donoExistente.setFoneCelular(donoComNovosDados.getFoneCelular());
                    donoExistente.setDataNascimento(donoComNovosDados.getDataNascimento());
                    donoExistente.setRazaoSocial(donoComNovosDados.getRazaoSocial());
                    donoExistente.setCpf(donoComNovosDados.getCpf());

                    return repository.save(donoExistente);
                })
                .orElse(null); 
    }
}