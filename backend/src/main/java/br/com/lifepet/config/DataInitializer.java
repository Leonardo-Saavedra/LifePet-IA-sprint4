package br.com.lifepet.config;

import br.com.lifepet.entity.Perfil;
import br.com.lifepet.entity.Usuario;
import br.com.lifepet.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner criarUsuarioTeste(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (usuarioRepository.findByEmail("vitor@email.com").isEmpty()) {

                Usuario usuario = new Usuario();

                usuario.setNome("Vitor");
                usuario.setEmail("vitor@email.com");
                usuario.setSenha(passwordEncoder.encode("123456"));
                usuario.setPerfil(Perfil.TUTOR);

                usuarioRepository.save(usuario);

                System.out.println("=================================");
                System.out.println("USUÁRIO DE TESTE CRIADO");
                System.out.println("Email: vitor@email.com");
                System.out.println("Senha: 123456");
                System.out.println("Perfil: TUTOR");
                System.out.println("=================================");
            }

            if (usuarioRepository.findByEmail("vet@lifepet.com").isEmpty()) {

                Usuario veterinario = new Usuario();

                veterinario.setNome("Dr. Veterinário");
                veterinario.setEmail("vet@lifepet.com");
                veterinario.setSenha(passwordEncoder.encode("123456"));
                veterinario.setPerfil(Perfil.VETERINARIO);

                usuarioRepository.save(veterinario);

                System.out.println("=================================");
                System.out.println("VETERINÁRIO DE TESTE CRIADO");
                System.out.println("Email: vet@lifepet.com");
                System.out.println("Senha: 123456");
                System.out.println("Perfil: VETERINARIO");
                System.out.println("=================================");
            }
        };
    }
}