package br.com.ifpe.eventos.modelo.stand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StandService {

    @Autowired
    private StandRepository repository;

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
       
        return repository.findByUsuarioUsername(username);
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