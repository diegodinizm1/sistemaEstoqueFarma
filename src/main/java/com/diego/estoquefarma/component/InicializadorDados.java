package com.diego.estoquefarma.component;

import com.diego.estoquefarma.model.Usuario;
import com.diego.estoquefarma.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder; // Importe o PasswordEncoder

@Component
public class InicializadorDados implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Adicione o PasswordEncoder

    public InicializadorDados(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            System.out.println("Nenhum usuário encontrado. Criando um usuário padrão...");

            Usuario usuarioPadrao = new Usuario();
            usuarioPadrao.setNomeCompleto("José Maia da Cruz Neto");
            usuarioPadrao.setLogin("josemaia");
            usuarioPadrao.setSenha(passwordEncoder.encode("070621"));// Defina um papel de administrador

            usuarioRepository.save(usuarioPadrao);

            System.out.println("Usuário 'josemaia' criado com sucesso!");
        } else {
            System.out.println("Já existem usuários no banco de dados. Nenhum usuário padrão foi criado.");
        }
    }
}