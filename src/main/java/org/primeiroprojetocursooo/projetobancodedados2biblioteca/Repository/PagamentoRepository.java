package org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository;

import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    // Soma total geral (você já tinha esse)
    @Query("SELECT SUM(p.valor) FROM Pagamento p")
    Double calcularFaturamentoTotal();

    // NOVO: Soma por intervalo de datas
    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE p.dataPagamento BETWEEN :inicio AND :fim")
    Double calcularFaturamentoPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}