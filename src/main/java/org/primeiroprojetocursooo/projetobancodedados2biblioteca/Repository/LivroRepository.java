package org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository;

import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Livro;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.LivroStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    // Buscar apenas livros disponíveis
    List<Livro> findByStatus(LivroStatus status);

    // Buscar livros de uma categoria específica
    List<Livro> findByCategoriaDescricaoContainingIgnoreCase(String nomeCategoria);

    boolean existsByIsbn(String isbn);
}