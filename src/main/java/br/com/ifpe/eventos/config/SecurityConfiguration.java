package br.com.ifpe.eventos.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.ifpe.eventos.modelo.seguranca.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(c -> c.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/dono").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/administrador").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/stand").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/stand").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stand/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stand/disponiveis").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stand/usuario").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stand/registered").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stand/selecao").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stand/disponiveis-evento").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/stand/processar-reserva").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/stand/batch-operation").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/stand-selection/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/stand-selection/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/api/evento").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/clientes/by-email/**").permitAll() 
                
                .requestMatchers(HttpMethod.POST, "/api/auth").permitAll() 
                
                .requestMatchers(HttpMethod.GET, "/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll() 

                .anyRequest().authenticated() 
            )
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )            
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
    
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);      
        return source;
    }
}