/*
 * =============================================================================
 * ENUM: LivroStatus
 * DESCRICAO: Define os estados possíveis de um livro no acervo.
 * Utilizado para controlar a disponibilidade de locação e regras de interface.
 * -----------------------------------------------------------------------------
 * ESTADOS:
 * - DISPONIVEL: Pronto para ser emprestado.
 * - LOCADO: Em posse de um usuário.
 * - MANUTENCAO: Em reparo (não disponível para locação).
 * - INDISPONIVEL: Retirado de circulação (extravio ou descarte).
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * ??/01/2026 | Erick Geovane    | Definição inicial dos status do acervo.
 * |                  |
 * =============================================================================
 */

package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums;

public enum LivroStatus {
    DISPONIVEL,
    LOCADO,
    MANUTENCAO,
    INDISPONIVEL
}