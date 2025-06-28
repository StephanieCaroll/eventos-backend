package br.com.ifpe.eventos.modelo.evento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional; 

@Service
public class EventoService {

    @Autowired
    private EventoRepository repository; 

    @Transactional 
    public Evento salvar(Evento evento) {
        evento.setHabilitado(Boolean.TRUE);
        return repository.save(evento);
    }

    public Evento findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Evento> findAll() {
        return repository.findAll(); 
    }

}
