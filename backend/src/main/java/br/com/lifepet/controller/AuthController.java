package br.com.lifepet.controller;

import br.com.lifepet.dto.LoginRequest;
import br.com.lifepet.dto.LoginResponse;
import br.com.lifepet.entity.Usuario;
import br.com.lifepet.repository.UsuarioRepository;
import br.com.lifepet.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(
                usuario.getEmail(),
                usuario.getPerfil().name()
        );

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        usuario.getPerfil().name()
                )
        );
    }
}
