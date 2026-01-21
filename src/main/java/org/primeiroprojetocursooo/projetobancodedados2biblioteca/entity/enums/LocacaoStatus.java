/*
 * =============================================================================
 * ENUM: LocacaoStatus
 * DESCRICAO: Representa o ciclo de vida de uma locação no sistema.
 * Essencial para o controle de devoluções, cálculos de faturamento e alertas.
 * -----------------------------------------------------------------------------
 * ESTADOS:
 * - ABERTA: Empréstimo ativo, dentro do prazo ou aguardando devolução.
 * - FINALIZADA: Livros devolvidos e pagamentos devidamente registrados.
 * - ATRASADA: Prazo de devolução excedido (possível incidência de multa).
 * - CANCELADA: Registro invalidado ou erro na operação inicial.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * ??/01/2026 | Erick Geovane    | Estruturação dos estados de locação e fluxo.
 * |                  |
 * =============================================================================
 */



package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums;

public enum LocacaoStatus {
    ABERTA,
    FINALIZADA,
    ATRASADA,
    CANCELADA
}