package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.LocacaoStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_locacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Locacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataLocacao;
    private LocalDate dataDevolucaoPrevista; // Nome mais claro
    private LocalDate dataDevolucaoReal;     // Para saber quando entregou de verdade

    @Enumerated(EnumType.STRING)
    private LocacaoStatus status;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_locacao_livro",
            joinColumns = @JoinColumn(name = "locacao_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    @Builder.Default // Garante que o Builder inicialize o Set vazio
    private Set<Livro> livros = new HashSet<>();

    @OneToOne(mappedBy = "locacao", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    /**
     * Lógica de negócio simples pode ficar na entidade.
     * Calcula multa ou valor total.
     */
    public Double getValorTotal() {
        if (livros == null || livros.isEmpty() || dataLocacao == null) return 0.0;

        // Se ainda não devolveu, calcula até hoje. Se devolveu, até a data real.
        LocalDate dataFim = (dataDevolucaoReal != null) ? dataDevolucaoReal : LocalDate.now();

        long dias = ChronoUnit.DAYS.between(dataLocacao, dataFim);
        dias = Math.max(1, dias); // Cobra no mínimo 1 dia

        double total = 0.0;
        for (Livro l : livros) {
            total += (l.getPreco() != null ? l.getPreco() : 0.0);
        }
        return total * dias;
    }
}