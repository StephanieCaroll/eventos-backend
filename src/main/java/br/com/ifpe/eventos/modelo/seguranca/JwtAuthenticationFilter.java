package br.com.ifpe.eventos.modelo.seguranca;

import java.io.IOException;
import java.util.Arrays; 
import java.util.List;   

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final List<String> PUBLIC_GET_ROUTES = Arrays.asList(
            "/api/evento",
            "/api/clientes/by-email/", 
            "/api-docs",
            "/swagger-ui"
    );

    private static final List<String> PUBLIC_POST_ROUTES = Arrays.asList(
            "/api/clientes",
            "/api/dono",
            "/api/adm",
            "/api/auth"
    );

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
                                   HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            for (String publicRoute : PUBLIC_GET_ROUTES) {
                if (publicRoute.endsWith("/")) { 
                    if (path.startsWith(publicRoute)) {
                        return true;
                    }
                } else if (path.equals(publicRoute) || path.startsWith(publicRoute + "/")) {
                   
                    return true;
                }
            }
        }

        if ("POST".equalsIgnoreCase(method)) {
            for (String publicRoute : PUBLIC_POST_ROUTES) {
                if (path.equals(publicRoute)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            try {
                throw new Exception("Token JWT ausente ou inválido.");
            } catch (Exception exception) {
                handlerExceptionResolver.resolveException(request, response, null, exception);
                return; 
            }
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
           
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}