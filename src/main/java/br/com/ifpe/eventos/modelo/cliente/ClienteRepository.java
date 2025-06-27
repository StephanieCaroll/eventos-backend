package br.com.ifpe.eventos.modelo.cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ifpe.eventos.modelo.acesso.Usuario; 

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Método para encontrar um Expositor pelo Usuario
    Optional<Cliente> findByUsuario(Usuario usuario);
}