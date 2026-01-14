package org.primeiroprojetocursooo.projetobancodedados2biblioteca.services;

import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository.UsuarioRepository;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    // Criptografia segura
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() == null && repository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }

        // Se a senha não estiver criptografada (não começa com $2a$), criptografa
        if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return repository.save(usuario);
    }

    public Usuario autenticar(String email, String senhaPura) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (passwordEncoder.matches(senhaPura, usuario.getSenha())) {
            return usuario;
        }
        throw new IllegalArgumentException("Senha incorreta");
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}