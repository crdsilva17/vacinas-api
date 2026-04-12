package br.com.municipio.vacinas.vacinas_api.security;

import java.io.IOException;
import java.util.Optional;

import br.com.municipio.vacinas.vacinas_api.repository.UsuarioRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenConfig tokenConfig;

    private final UsuarioRepository repository;

    public SecurityFilter(TokenConfig tokenConfig, UsuarioRepository repository) {

        this.tokenConfig = tokenConfig;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (Strings.isNotEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            Optional<JwtUserData> userData = tokenConfig.validateToken(token);

            if (userData.isPresent()) {
                JwtUserData jwtUserData = userData.get();
                UserDetails user = repository.findById(jwtUserData.userId()).orElseThrow(
                        () -> new RuntimeException("Usuário não encontrado!")
                );
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        jwtUserData, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
