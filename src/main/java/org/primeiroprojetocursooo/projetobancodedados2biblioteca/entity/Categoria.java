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