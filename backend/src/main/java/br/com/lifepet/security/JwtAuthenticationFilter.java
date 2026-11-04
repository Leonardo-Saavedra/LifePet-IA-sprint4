package br.com.lifepet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        System.out.println("=================================");
        System.out.println("REQUISIÇÃO: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("AUTHORIZATION EXISTE: " + (authorization != null));
        System.out.println("=================================");

        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("TOKEN NÃO ENCONTRADO.");
            System.out.println("=================================");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {

            String email = jwtService.getEmail(token);
            String perfil = jwtService.getPerfil(token);

            System.out.println("USUARIO JWT: " + email);
            System.out.println("PERFIL JWT: " + perfil);

            String role = "ROLE_" + perfil;

            System.out.println("ROLE GERADA: " + role);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(role)
                            )
                    );

            System.out.println(
                    "AUTORIDADES: " +
                            authentication.getAuthorities()
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            System.out.println(
                    "USUARIO AUTENTICADO: " +
                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getName()
            );

            System.out.println(
                    "AUTORIDADES NO CONTEXTO: " +
                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getAuthorities()
            );

            System.out.println("=================================");

        } catch (Exception e) {

            System.out.println("ERRO AO VALIDAR JWT:");
            System.out.println(e.getMessage());
            System.out.println("=================================");

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}