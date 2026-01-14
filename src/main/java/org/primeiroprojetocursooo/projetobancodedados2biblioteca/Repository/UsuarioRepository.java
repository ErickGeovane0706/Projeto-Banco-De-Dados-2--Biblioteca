package org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository;

import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Para Login
    Optional<Usuario> findByEmail(String email);

    // Para validações
    boolean existsByEmail(String email);
    boolean existsByMatricula(String matricula);

    // Para busca na tela
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}