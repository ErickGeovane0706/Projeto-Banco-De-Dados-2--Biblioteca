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