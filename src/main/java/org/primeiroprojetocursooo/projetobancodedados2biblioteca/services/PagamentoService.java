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

    @Transactional
    public Pagamento registrarPagamento(Locacao locacao) {
        // 1. Verifica se já existe pagamento para evitar duplicidade
        if (locacao.getPagamento() != null) {
            throw new IllegalArgumentException("Esta locação já foi paga!");
        }

        // 2. Cria o pagamento
        // Ao usar setLocacao, a entidade Pagamento calcula o valor automaticamente
        // baseada na regra de dias ou preço do livro definida na entidade Locacao.
        Pagamento pagamento = Pagamento.builder()
                .dataPagamento(LocalDate.now())
                .locacao(locacao)
                .build();

        // Força o cálculo do valor caso o Builder não tenha disparado o setter
        pagamento.setLocacao(locacao);

        // 3. Validação: Só salva se houver valor > 0 (opcional)
        // Se sua biblioteca cobra 0 reais por empréstimo pontual, pode remover esse if.
        if (pagamento.getValor() == 0.0) {
            // Opcional: Decidir se salva recibo de R$ 0,00 ou não.
            // Geralmente salvamos para ter histórico.
        }

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