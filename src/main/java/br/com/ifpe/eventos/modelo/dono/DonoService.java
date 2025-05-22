package br.com.ifpe.eventos.modelo.dono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
@Service
public class DonoService {
     @Autowired
   private DonoRepository repository;

   @Transactional
   public Dono save(Dono dono) {

       dono.setHabilitado(Boolean.TRUE);
       return repository.save(dono);
   }
}
