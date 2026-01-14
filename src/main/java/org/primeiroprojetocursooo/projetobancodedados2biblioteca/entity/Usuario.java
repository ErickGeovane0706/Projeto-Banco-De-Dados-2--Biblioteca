package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.TipoUsuario;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tb_usuario")
@Data                 // Lombok: Gera Getters, Setters, ToString, etc.
@NoArgsConstructor    // Obrigatório para o Spring Data JPA
@AllArgsConstructor   // Útil para testes e construtores rápidos
@Builder              // Permite construção fluente de objetos
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false) // A senha é obrigatória agora
    private String senha;

    private String telefone;

    @Column(unique = true)
    private String matricula;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo; // ALUNO, PROFESSOR ou ADMINISTRADOR

    // Relacionamento com Locações
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    @ToString.Exclude // Evita loop infinito de memória
    @EqualsAndHashCode.Exclude
    private Set<Locacao> locacoes;
}