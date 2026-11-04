package br.com.lifepet.controller;

import br.com.lifepet.entity.Usuario;
import br.com.lifepet.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        usuarioSalvo.setSenha(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }
}