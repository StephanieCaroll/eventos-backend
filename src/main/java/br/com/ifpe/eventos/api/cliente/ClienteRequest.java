package br.com.ifpe.eventos.api.cliente;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import br.com.ifpe.eventos.modelo.cliente.Cliente;
import br.com.ifpe.eventos.modelo.acesso.Usuario;

// import lombok.AllArgsConstructor; 
// import lombok.Builder;       
// import lombok.Getter;         
// import lombok.NoArgsConstructor;
// import lombok.Setter;        

// @Getter
// @Setter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
public class ClienteRequest {

    private String nome;

    private UsuarioRequest usuario; 

    @JsonFormat(pattern = "dd/MM/yyyy") 
    private LocalDate dataNascimento;

    private String foneCelular;

    public Cliente build() {
       
        Usuario novoUsuario = Usuario.builder()
            .username(this.usuario.getUsername())
            .password(this.usuario.getPassword()) 
            .build();

        return Cliente.builder()
            .nome(this.nome)
            .usuario(novoUsuario)
            .dataNascimento(this.dataNascimento)
            .foneCelular(this.foneCelular)
            .build();
    }
    
    public static class UsuarioRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; } 
        public void setPassword(String password) { this.password = password; }
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public UsuarioRequest getUsuario() { return usuario; }
    public void setUsuario(UsuarioRequest usuario) { this.usuario = usuario; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getFoneCelular() { return foneCelular; }
    public void setFoneCelular(String foneCelular) { this.foneCelular = foneCelular; }
}