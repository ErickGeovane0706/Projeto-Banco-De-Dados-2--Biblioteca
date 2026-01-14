package org.primeiroprojetocursooo.projetobancodedados2biblioteca.services;

import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository.LivroRepository;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Livro;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.LivroStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;

    @Transactional
    public Livro salvar(Livro livro) {
        // Regra: Se for novo, nasce DISPONIVEL
        if (livro.getId() == null) {
            livro.setStatus(LivroStatus.DISPONIVEL);
        }
        return repository.save(livro);
    }

    public List<Livro> listarTodos() {
        return repository.findAll();
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> listarDisponiveis() {
        return repository.findByStatus(LivroStatus.DISPONIVEL);
    }

    @Transactional
    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}