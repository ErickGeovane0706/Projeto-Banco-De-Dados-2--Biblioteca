package org.primeiroprojetocursooo.projetobancodedados2biblioteca.services;

import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository.LivroRepository;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository.LocacaoRepository;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Livro;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Locacao;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;

import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.LocacaoStatus;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.LivroStatus;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.TipoUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocacaoService {

    private final LocacaoRepository locacaoRepository;
    private final LivroRepository livroRepository;

    @Transactional
    public Locacao realizarEmprestimo(Usuario usuario, List<Livro> livros) {
        if (livros.isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um livro.");
        }

        // 1. Calcula a data de devolução baseada no TIPO DO USUÁRIO
        int diasParaDevolucao = (usuario.getTipo() == TipoUsuario.PROFESSOR) ? 15 : 7;

        Locacao locacao = Locacao.builder()
                .usuario(usuario)
                .livros(new java.util.HashSet<>(livros))
                .dataLocacao(LocalDate.now())
                .dataDevolucaoPrevista(LocalDate.now().plusDays(diasParaDevolucao))
                .status(LocacaoStatus.ABERTA) // Alterei o enum para ABERTA ou LOCADA (verifique seu enum)
                .build();

        // 2. Atualiza o status dos livros para "LOCADO"
        for (Livro livro : livros) {
            if (livro.getStatus() != LivroStatus.DISPONIVEL) {
                throw new IllegalArgumentException("O livro " + livro.getTitulo() + " não está disponível.");
            }
            livro.setStatus(LivroStatus.LOCADO);
            livroRepository.save(livro);
        }

        return locacaoRepository.save(locacao);
    }

    @Transactional
    public void realizarDevolucao(Locacao locacao) {
        // 1. Marca como devolvido
        locacao.setDataDevolucaoReal(LocalDate.now());
        locacao.setStatus(LocacaoStatus.FINALIZADA);

        // 2. Libera os livros
        for (Livro livro : locacao.getLivros()) {
            livro.setStatus(LivroStatus.DISPONIVEL);
            livroRepository.save(livro);
        }

        locacaoRepository.save(locacao);
    }

    public List<Locacao> listarTodas() {
        return locacaoRepository.findAll();
    }
}