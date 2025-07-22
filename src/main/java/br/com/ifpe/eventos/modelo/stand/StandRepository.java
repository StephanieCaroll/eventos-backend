package br.com.ifpe.eventos.modelo.stand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StandRepository extends JpaRepository<Stand, Long> {

    List<Stand> findByEventoIsNull();

    List<Stand> findAllById(Iterable<Long> ids);

    @Query("SELECT s FROM Stand s WHERE s.usuario.username = :username AND s.habilitado = true")
    List<Stand> findByUsuarioUsername(@Param("username") String username);

    @Query("SELECT s FROM Stand s WHERE s.usuario.username = :username AND s.evento.id = :eventId AND s.habilitado = true")
    List<Stand> findByUsuarioUsernameAndEventoId(@Param("username") String username, @Param("eventId") Long eventId);
}