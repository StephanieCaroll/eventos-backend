package br.com.ifpe.eventos.modelo.stand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StandRepository extends JpaRepository<Stand, Long> {
    
    List<Stand> findByEventoIsNull();
    
    List<Stand> findAllById(Iterable<Long> ids);
}