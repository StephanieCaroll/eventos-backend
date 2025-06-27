package br.com.ifpe.eventos.api.acesso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.eventos.modelo.acesso.Usuario; // Importar UserDetails
import br.com.ifpe.eventos.modelo.acesso.UsuarioService;
import br.com.ifpe.eventos.modelo.seguranca.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    private final JwtService jwtService;
    
    private UsuarioService usuarioService;

    public AuthenticationController(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Map<Object, Object> signin(@RequestBody AuthenticationRequest data) {
    
        // O usuarioService.authenticate deve retornar um UserDetails ou uma classe que implemente UserDetails
        // para que o JwtService possa extrair as authorities (papéis).
        Usuario authenticatedUser = usuarioService.authenticate(data.getUsername(), data.getPassword());

        // Gera o token JWT. O JwtService agora adicionará os papéis automaticamente.
        // É importante que authenticatedUser seja um UserDetails (ou compatível com ele.getAuthorities())
        String jwtToken = jwtService.generateToken(authenticatedUser);

        // --- PARTE CRÍTICA: Obtém o timestamp de expiração do token em SEGUNDOS ---
        // Este é o valor que o frontend precisa para 'tokenExpiresIn'
        long tokenExpiresInSeconds = jwtService.getTokenExpirationTimestampSeconds(jwtToken);

        Map<Object, Object> loginResponse = new HashMap<>();
        loginResponse.put("username", authenticatedUser.getUsername());
        loginResponse.put("token", jwtToken);
        // ALTERADO: Envia o timestamp ABSOLUTO de expiração em SEGUNDOS
        loginResponse.put("tokenExpiresIn", tokenExpiresInSeconds); 

        return loginResponse;
    }     
}
