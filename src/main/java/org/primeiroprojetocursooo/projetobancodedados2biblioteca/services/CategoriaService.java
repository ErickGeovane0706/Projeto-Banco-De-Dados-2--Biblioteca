package org.primeiroprojetocursooo.projetobancodedados2biblioteca.services;

import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository.CategoriaRepository;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Categoria;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    @Transactional
    public Categoria salvar(Categoria categoria) {
        return repository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }
}