package br.com.ifpe.eventos.modelo.seguranca;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration; // Esta é a DURAÇÃO em milissegundos (ex: 3600000ms para 1 hora)

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Este método agora coleta os papéis e os adiciona aos claims antes de gerar o token.
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // --- PARTE CRÍTICA: Adicionar os papéis (roles) aos claims ---
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Converte GrantedAuthority para String (ex: "ROLE_EXPOSITOR")
                .collect(Collectors.toList());
        claims.put("roles", roles); // Adiciona o claim "roles" ao payload do JWT
        
        return generateToken(claims, userDetails);
    }

    // Este método sobrecarregado usará os extraClaims fornecidos (que agora devem conter os papéis)
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Este método retorna a DURAÇÃO configurada em milissegundos.
    // Não será usado diretamente para 'tokenExpiresIn' no frontend.
    public long getExpirationTime() {
        return jwtExpiration;
    }

    // --- NOVO MÉTODO: Extrai o timestamp ABSOLUTO de expiração do token (em SEGUNDOS) ---
    // O frontend espera este valor para o campo 'tokenExpiresIn'.
    public long getTokenExpirationTimestampSeconds(String token) {
        Date expirationDate = extractExpiration(token);
        // Retorna o timestamp em segundos (JWT 'exp' é em segundos)
        return expirationDate.getTime() / 1000; 
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationDurationMillis) {
        // 'expirationDurationMillis' é a duração (ex: 3600000ms).
        // setExpiration espera um Date, que é System.currentTimeMillis() + duração.
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationDurationMillis)) // Define o claim 'exp' no token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
