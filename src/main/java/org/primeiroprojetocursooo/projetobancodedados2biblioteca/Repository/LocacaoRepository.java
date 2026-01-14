package org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository;


import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Locacao;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.LocacaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Integer> {

    // Histórico de um usuário específico
    List<Locacao> findByUsuario(Usuario usuario);

    // Buscar locações em aberto (para cobrar devolução)
    List<Locacao> findByStatus(LocacaoStatus status);
}