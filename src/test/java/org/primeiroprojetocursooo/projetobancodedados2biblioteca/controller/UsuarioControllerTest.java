/*
 * =============================================================================
 * CLASSE: UsuarioControllerTest
 * DESCRICAO: Suíte de testes para a gestão de usuários e perfis.
 * Valida o preenchimento de formulários, seleção de Enums e regras de
 * exclusão vinculadas ao UsuarioService.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 31/01/2026 | Erivan Barros  | Implementação dos testes de CRUD e validação de Enums.
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
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.TipoUsuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController controller;

    // Componentes FXML para Mock manual
    private TextField txtNome;
    private TextField txtEmail;
    private PasswordField txtSenha;
    private ComboBox<TipoUsuario> comboTipo;
    private Label lblMensagem;
    private TableView<Usuario> tabelaUsuarios;

    @BeforeAll
    static void initJavaFX() {
        if (!Platform.isFxApplicationThread()) {
            try {
                Platform.startup(() -> {});
            } catch (IllegalStateException ignored) {}
        }
    }

    @BeforeEach
    void setup() throws Exception {
        txtNome = new TextField();
        txtEmail = new TextField();
        txtSenha = new PasswordField();
        comboTipo = new ComboBox<>();
        lblMensagem = new Label();
        tabelaUsuarios = new TableView<>();

        // Injeção via reflexão para os campos privados @FXML
        setField(controller, "txtNome", txtNome);
        setField(controller, "txtEmail", txtEmail);
        setField(controller, "txtSenha", txtSenha);
        setField(controller, "comboTipo", comboTipo);
        setField(controller, "lblMensagem", lblMensagem);
        setField(controller, "tabelaUsuarios", tabelaUsuarios);
    }

    @Test
    void deveExibirErroQuandoCamposObrigatoriosEstiveremVazios() {
        // GIVEN: Deixando campos vazios
        txtNome.setText("");

        // WHEN
        controller.salvarUsuario();

        // THEN
        assertEquals("Preencha os campos obrigatórios (*)", lblMensagem.getText());
        verify(usuarioService, never()).salvar(any());
    }

    @Test
    void deveSalvarUsuarioComSucessoQuandoDadosForemValidos() {
        // GIVEN
        txtNome.setText("Erivan Barros");
        txtEmail.setText("erivan@email.com");
        txtSenha.setText("senha123");

        // BUG --> preciso dar uma olhada nisso ainda, não está certo.
        // --- >>>> comboTipo.setValue(TipoUsuario.ADMIN);

        // WHEN
        controller.salvarUsuario();

        // THEN
        verify(usuarioService, times(1)).salvar(any(Usuario.class));
        assertEquals("Usuário salvo com sucesso!", lblMensagem.getText());
        assertTrue(txtNome.getText().isEmpty()); // Garante que limpou o formulário
    }

    @Test
    void deveTratarErroDeExclusaoParaUsuarioComPendencias() {
        // GIVEN
        Usuario mockUsuario = new Usuario();
        mockUsuario.setId(1);
        tabelaUsuarios.getItems().add(mockUsuario);
        tabelaUsuarios.getSelectionModel().select(mockUsuario);

        // Simula a exceção que o banco/service dispararia se houvesse um empréstimo ativo
        doThrow(new RuntimeException("Constraint Violation")).when(usuarioService).deletar(1);

        // WHEN
        controller.deletarUsuario();

        // THEN
        assertEquals("Não é possível excluir usuário com empréstimos ativos.", lblMensagem.getText());
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}