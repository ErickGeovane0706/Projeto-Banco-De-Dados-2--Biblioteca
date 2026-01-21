/*
 * =============================================================================
 * ENUM: TipoUsuario
 * DESCRICAO: Define as categorias de usuários do sistema e seus níveis de acesso.
 * Utilizado para aplicar regras de negócio distintas (ex: prazos de entrega
 * ou permissões de gerenciamento).
 * -----------------------------------------------------------------------------
 * CATEGORIAS:
 * - ALUNO: Usuário padrão, sujeito a limites de empréstimo comuns.
 * - PROFESSOR: Categoria com prazos de devolução diferenciados.
 * - ADMINISTRADOR: Acesso total às funções de faturamento e gestão de acervo.
 * - VISITANTE: Acesso restrito apenas para consulta e visualização.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * ??/01/2026 | Erick Geovane    | Definição dos perfis de usuário do sistema.
 * |                  |
 * =============================================================================
 */

package org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums;

public enum TipoUsuario {
    ALUNO,
    PROFESSOR,
    ADMINISTRADOR,
    VISITANTE
}