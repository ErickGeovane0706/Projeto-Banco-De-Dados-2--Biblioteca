package org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository;


import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByDescricao(String descricao);
}
