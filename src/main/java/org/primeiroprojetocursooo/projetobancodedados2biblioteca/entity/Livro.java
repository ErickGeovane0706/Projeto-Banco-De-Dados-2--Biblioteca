/*
 * =============================================================================
 * ENTIDADE: Livro
 * DESCRICAO: Representa o item principal do acervo da biblioteca.
 * Armazena dados bibliográficos, estado de conservação e disponibilidade.
 * -----------------------------------------------------------------------------
 * RELACIONAMENTOS:
 * - ManyToOne: Cada livro pertence a uma única Categoria.
 * - ManyToMany: Um livro pode fazer parte de diversas locações ao longo do tempo.
 * - Enumerated: O campo 'status' utiliza o Enum LivroStatus (STRING no banco).
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * ??/01/2026 | Erick Geovane    | Mapeamento da entidade, inclusão de novos campos
 * |                  | (ISBN, Editora) e relação ManyToMany.
 * =============================================================================
 */


package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.LivroStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tb_livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String titulo;

    private String autor;   // Novo
    private String editora; // Novo
    private String isbn;    // Novo (código de barras)

    private Double preco;

    private LocalDate dataLancamento; // Melhor que String

    @Enumerated(EnumType.STRING)
    private LivroStatus status;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToMany(mappedBy = "livros", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Locacao> locacoes;
}