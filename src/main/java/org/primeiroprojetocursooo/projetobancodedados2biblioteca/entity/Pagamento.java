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
    private Locacao locacao;
}