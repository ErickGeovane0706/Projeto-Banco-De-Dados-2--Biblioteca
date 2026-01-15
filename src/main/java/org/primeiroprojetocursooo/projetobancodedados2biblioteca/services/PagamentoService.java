package org.primeiroprojetocursooo.projetobancodedados2biblioteca.services;

import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository.PagamentoRepository;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Locacao;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Pagamento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repository;


    public Double obterFaturamentoPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            return 0.0;
        }
        Double total = repository.calcularFaturamentoPorPeriodo(inicio, fim);
        return (total != null) ? total : 0.0;
    }

    @Transactional
    public Pagamento registrarPagamento(Locacao locacao) {
        // 1. Verifica duplicidade
        if (locacao.getPagamento() != null) {
            throw new IllegalArgumentException("Esta locação já foi paga!");
        }

        // 2. Cria o pagamento
        Pagamento pagamento = Pagamento.builder()
                .dataPagamento(LocalDate.now())
                .locacao(locacao)
                .build();

        // --- CORREÇÃO AQUI ---
        // Pegamos o cálculo inteligente da Locacao e setamos no Pagamento
        Double valorCalculado = locacao.getValorTotal();
        pagamento.setValor(valorCalculado);
        // ---------------------

        return repository.save(pagamento);
    }

    public Double obterFaturamentoTotal() {
        Double total = repository.calcularFaturamentoTotal();
        return (total != null) ? total : 0.0;
    }

    public List<Pagamento> listarTodos() {
        return repository.findAll();
    }
}