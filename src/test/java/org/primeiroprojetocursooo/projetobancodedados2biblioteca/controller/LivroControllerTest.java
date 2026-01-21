/*
 * =============================================================================
 * CLASSE: LivroControllerTest
 * DESCRICAO: Testes Unitários para o Controle de Livros (CRUD).
 * Valida o fluxo de persistência de dados e a integridade da lista observável.
 * Garante que as interações da interface reflitam corretamente no LivroService.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 31/01/2026 | Erivan Barros  | Criação dos testes de CRUD e Mock de serviços.
 * =============================================================================
 */


package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;



import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Livro;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.LivroService;

@ExtendWith(MockitoExtension.class)
public class LivroControllerTest {

    @Mock
    private LivroService livroService;

    @InjectMocks
    private LivroController controller;

    // Elementos @FXML
    private TextField txtTitulo;
    private TextField txtAutor;
    private Label lblMensagem;
    private TableView<Livro> tabelaLivros;

    @BeforeAll
    static void initJavaFX() {
        if (!Platform.isFxApplicationThread()) {
            try {
                Platform.startup(() -> {});
            } catch (IllegalStateException e) {}
        }
    }

    @BeforeEach
    void setup() throws Exception {
        txtTitulo = new TextField();
        txtAutor = new TextField();
        lblMensagem = new Label();
        tabelaLivros = new TableView<>();

        setField(controller, "txtTitulo", txtTitulo);
        setField(controller, "txtAutor", txtAutor);
        setField(controller, "lblMensagem", lblMensagem);
        setField(controller, "tabelaLivros", tabelaLivros);
    }

    @Test
    void deveSalvarLivroComSucesso() {
        // GIVEN
        txtTitulo.setText("O Senhor dos Anéis");
        txtAutor.setText("J.R.R. Tolkien");

        // WHEN
        controller.salvarLivro();

        // THEN
        verify(livroService, times(1)).salvar(any(Livro.class));
        assertEquals("Livro saved successfully!", lblMensagem.getText()); // Ajuste conforme sua string exata
        assertTrue(txtTitulo.getText().isEmpty()); // Verifica se limpou campos
    }

    @Test
    void deveMostrarMensagemErroQuandoServiceFalhar() {
        // GIVEN
        txtTitulo.setText("Erro Teste");
        doThrow(new RuntimeException("Falha no banco")).when(livroService).salvar(any());

        // WHEN
        controller.salvarLivro();

        // THEN
        assertTrue(lblMensagem.getText().contains("Erro ao salvar"));
        assertEquals("red", lblMensagem.getStyle().contains("red") ? "red" : "");
    }

    @Test
    void devePedirSelecaoAoTentarDeletarSemLivroSelecionado() {
        // GIVEN: Nenhuma linha selecionada na tabela
        // WHEN
        controller.deletarLivro();

        // THEN
        assertEquals("Selecione um livro na tabela para excluir.", lblMensagem.getText());
        verify(livroService, never()).deletar(anyInt());
    }

    // Helper para reflexão
    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}