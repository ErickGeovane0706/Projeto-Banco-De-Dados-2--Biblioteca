package org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository;


import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Locacao;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    // Busca um pagamento associado a uma locação específica
    Optional<Pagamento> findByLocacao(Locacao locacao);

    // Query personalizada para somar todo o faturamento da biblioteca (Útil para Dashboard)
    @Query("SELECT SUM(p.valor) FROM Pagamento p")
    Double calcularFaturamentoTotal();
}
