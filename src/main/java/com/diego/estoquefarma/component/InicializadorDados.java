package com.diego.estoquefarma.component;

import com.diego.estoquefarma.model.Usuario;
import com.diego.estoquefarma.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class InicializadorDados implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public InicializadorDados(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            System.out.println("Nenhum usuário encontrado. Criando um usuário padrão...");

            String nomeCompleto = System.getenv("DEFAULT_USER_FULL_NAME");
            String login = System.getenv("DEFAULT_USER_LOGIN");
            String senha = System.getenv("DEFAULT_USER_PASSWORD");

            if (nomeCompleto == null || login == null || senha == null) {
                System.out.println("Erro: Variáveis de ambiente para o usuário padrão não estão definidas.");
                return;
            }

            Usuario usuarioPadrao = new Usuario();
            usuarioPadrao.setNomeCompleto(nomeCompleto);
            usuarioPadrao.setLogin(login);
            usuarioPadrao.setSenha(passwordEncoder.encode(senha));

            usuarioRepository.save(usuarioPadrao);

            System.out.println("Usuário '" + login + "' criado com sucesso!");
        } else {
            System.out.println("Já existem usuários no banco de dados. Nenhum usuário padrão foi criado.");
        }
    }
}