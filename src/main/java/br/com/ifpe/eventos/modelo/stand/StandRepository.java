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

    // Buscar stands cadastrados para um evento específico pelo gerenciador
    @Query("SELECT s FROM Stand s WHERE s.evento.id = :eventId AND s.habilitado = true")
    List<Stand> findByEventoId(@Param("eventId") Long eventId);

    // Buscar stands disponíveis para um evento (sem usuário associado)
    @Query("SELECT s FROM Stand s WHERE s.evento.id = :eventId AND s.usuario IS NULL AND s.habilitado = true")
    List<Stand> findStandsDisponiveisParaEvento(@Param("eventId") Long eventId);

    // Buscar stands ocupados por um usuário em um evento específico
    @Query("SELECT s FROM Stand s WHERE s.evento.id = :eventId AND s.usuario.username = :username AND s.habilitado = true")
    List<Stand> findStandsOcupadosPorUsuarioNoEvento(@Param("eventId") Long eventId, @Param("username") String username);
}