package br.com.ifpe.eventos.modelo.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.eventos.modelo.acesso.Perfil;
import br.com.ifpe.eventos.modelo.acesso.PerfilRepository;
import br.com.ifpe.eventos.modelo.acesso.UsuarioService;
import jakarta.transaction.Transactional;

@Service
public class AdmService {

   @Autowired
   private AdmRepository repository;

  @Autowired
   private UsuarioService usuarioService;

   @Autowired
   private PerfilRepository perfilUsuarioRepository;

   @Transactional
   public Adm save(Adm adm) {

 usuarioService.save(adm.getUsuario());

        for (Perfil perfil : adm.getUsuario().getRoles()) {
            perfil.setHabilitado(Boolean.TRUE);
            perfilUsuarioRepository.save(perfil);
        }


       adm.setHabilitado(Boolean.TRUE);
       return repository.save(adm);
   }

}
