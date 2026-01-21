/*
 * =============================================================================
 * ENTIDADE: Categoria
 * DESCRICAO: Representa as categorias/gêneros do acervo (Ex: Romance, Técnico).
 * Define o agrupamento lógico dos livros para fins de organização e busca.
 * -----------------------------------------------------------------------------
 * RELACIONAMENTOS:
 * - OneToMany: Uma categoria possui diversos livros associados.
 * - FetchType.EAGER: Carregamento automático da lista de livros ao buscar categoria.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * ??/01/2026 | Erick Geovane    | Mapeamento da entidade e relação com Livro.
 * |                  |
 * =============================================================================
 */


package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tb_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Livro> livros;
}