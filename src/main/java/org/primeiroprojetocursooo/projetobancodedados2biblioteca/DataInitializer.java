package org.primeiroprojetocursooo.projetobancodedados2biblioteca;

import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.Repository.*;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.*;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.*; // Importa os Enums corretos (LivroStatus, etc)
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set; // <--- IMPORTANTE: Necessário para criar o conjunto de livros

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final LocacaoRepository locacaoRepository;
    private final PagamentoRepository pagamentoRepository;

    @Override
    public void run(String... args) throws Exception {

        // --- 1. CRIAR USUÁRIO ADMIN (Se não existir) ---
        if (usuarioRepository.count() == 0) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            Usuario admin = new Usuario();
            admin.setNome("Administrador Sistema");
            admin.setEmail("admin@email.com");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setMatricula("ADM001");
            admin.setTipo(TipoUsuario.ADMINISTRADOR); // Corrigido para TipoUsuario

            usuarioRepository.save(admin);
            System.out.println(">>> ADMIN CRIADO: admin@email.com / 123456 <<<");
        }

        // --- 2. CRIAR DADOS PARA O GRÁFICO DE FATURAMENTO ---
        // Verifica se já tem o livro de teste para não duplicar
        if (livroRepository.findByTituloContainingIgnoreCase("Livro Faturamento").isEmpty()) {
            try {
                criarDadosFinanceiros();
            } catch (Exception e) {
                System.out.println("Erro ao criar dados de teste: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void criarDadosFinanceiros() {
        System.out.println(">>> GERANDO DADOS DE TESTE... <<<");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 1. Criar Livro
        Livro livro = new Livro();
        livro.setTitulo("Livro Faturamento"); // Título único
        livro.setAutor("Autor Teste");
        livro.setIsbn("FAT-001");
        livro.setEditora("Editora Teste");
        livro.setStatus(LivroStatus.DISPONIVEL); // Corrigido para LivroStatus
        livro = livroRepository.save(livro);

        // 2. Criar Usuário Pagador
        Usuario usuario = new Usuario();
        usuario.setNome("Cliente Pagador");
        usuario.setEmail("cliente@pagador.com");
        usuario.setSenha(encoder.encode("123"));
        usuario.setMatricula("PAG999");
        usuario.setTipo(TipoUsuario.ALUNO); // Corrigido para TipoUsuario
        usuario = usuarioRepository.save(usuario);

        // 3. Pagamento HOJE (R$ 50,00)
        Locacao loc1 = new Locacao();
        loc1.setUsuario(usuario);

        // --- AQUI ESTAVA O ERRO DO PRINT ---
        // Usamos Set.of() para transformar o único livro em uma lista (Set)
        loc1.setLivros(Set.of(livro));

        loc1.setDataLocacao(LocalDate.now().minusDays(7));
        loc1.setDataDevolucaoPrevista(LocalDate.now());
        loc1.setDataDevolucaoReal(LocalDate.now());
        loc1.setStatus(LocacaoStatus.FINALIZADA); // Corrigido para LocacaoStatus
        loc1 = locacaoRepository.save(loc1);

        Pagamento pag1 = new Pagamento();
        pag1.setLocacao(loc1);
        pag1.setValor(50.00);
        pag1.setDataPagamento(LocalDate.now()); // Data de Hoje
        // REMOVIDO: pag1.setFormaPagamento(...) pois sua entidade Pagamento não tem esse campo
        pagamentoRepository.save(pag1);


        // 4. Pagamento MÊS PASSADO (R$ 100,00)
        Locacao loc2 = new Locacao();
        loc2.setUsuario(usuario);
        loc2.setLivros(Set.of(livro)); // Set.of resolve o problema novamente
        loc2.setDataLocacao(LocalDate.now().minusMonths(1).minusDays(7));
        loc2.setDataDevolucaoPrevista(LocalDate.now().minusMonths(1));
        loc2.setDataDevolucaoReal(LocalDate.now().minusMonths(1));
        loc2.setStatus(LocacaoStatus.FINALIZADA);
        loc2 = locacaoRepository.save(loc2);

        Pagamento pag2 = new Pagamento();
        pag2.setLocacao(loc2);
        pag2.setValor(100.00);
        pag2.setDataPagamento(LocalDate.now().minusMonths(1)); // Data Passada
        pagamentoRepository.save(pag2);

        System.out.println(">>> DADOS FINANCEIROS CRIADOS COM SUCESSO! <<<");
    }
}