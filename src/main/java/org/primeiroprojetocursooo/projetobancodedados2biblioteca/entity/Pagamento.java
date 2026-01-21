/*
 * =============================================================================
 * ENTIDADE: Pagamento
 * DESCRICAO: Registra a liquidação financeira de uma locação. Armazena o valor
 * final recebido e a data da transação para fins de faturamento.
 * -----------------------------------------------------------------------------
 * RELACIONAMENTOS:
 * - OneToOne: Cada pagamento está estritamente vinculado a uma única Locação.
 * - JoinColumn: A chave estrangeira 'locacao_id' é única, garantindo a integridade.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * ??/01/2026 | Erick Geovane    | Mapeamento da entidade e vínculo com Locacao.
 * |                  |
 * =============================================================================
 */


package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataPagamento;

    private Double valor; // Valor final pago

    @OneToOne
    @JoinColumn(name = "locacao_id", unique = true)
    @ToString.Exclude // Evita loop
    @EqualsAndHashCode.Exclude
    private Locacao locacao;
}