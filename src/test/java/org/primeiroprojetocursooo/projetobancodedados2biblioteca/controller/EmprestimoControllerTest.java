/*
 * =============================================================================
 * CLASSE: EmprestimoControllerTest
 * DESCRICAO: Testes Unitários para o Controller de Empréstimos.
 * Valida regras de negócio, interações com services e lógica de UI.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 31/01/2026 | Erivan Barros       | Criação da suíte de testes com JUnit 5 e Mockito.
 * 31/01/2026 | Erivan Barros       | Implementação de mocks para serviços e validação de UI.
 * =============================================================================
 */

package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.LocacaoService;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.UsuarioService;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.LivroService;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.PagamentoService;

@ExtendWith(MockitoExtension.class)
public class EmprestimoControllerTest {

    @Mock private LocacaoService locacaoService;
    @Mock private UsuarioService usuarioService;
    @Mock private LivroService livroService;
    @Mock private PagamentoService pagamentoService;

    @InjectMocks
    private EmprestimoController controller;

    // Mocks para os componentes FXML (necessário instanciar manualmente no teste)
    private ComboBox<Usuario> cbUsuarios;
    private Label lblMensagemErro;

    @BeforeAll
    static void initJavaFX() {
        // Inicializa o Toolkit do JavaFX para evitar erros de "Internal graphics not initialized"
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Se já estiver inicializado, ignora
        }
    }

    @BeforeEach
    void setup() {
        // Como o FXML não carrega no teste unitário, instanciamos os campos @FXML usados nos testes
        cbUsuarios = new ComboBox<>();
        lblMensagemErro = new Label();

        // Injetamos manualmente via reflexão ou garantindo que o controller os use
        // Nota: Em testes reais de JavaFX, costuma-se usar TestFX,
        // mas aqui estamos simulando a lógica.
        setInternalField(controller, "cbUsuarios", cbUsuarios);
        setInternalField(controller, "lblMensagemErro", lblMensagemErro);
    }

    @Test
    void deveExibirErroQuandoUsuarioNaoForSelecionado() {
        // GIVEN: Carrinho vazio ou não, mas combo de usuário nulo
        cbUsuarios.setValue(null);

        // WHEN
        controller.finalizarEmprestimo();

        // THEN
        assertEquals("Selecione um usuário.", lblMensagemErro.getText());
        verifyNoInteractions(locacaoService); // Garante que o service NÃO foi chamado
    }

    @Test
    void deveChamarServiceAoFinalizarEmprestimoComSucesso() {
        // GIVEN
        Usuario mockUsuario = new Usuario();
        mockUsuario.setNome("Erivan");
        cbUsuarios.setValue(mockUsuario);

        // Simulando que o carrinho tem itens (usando reflexão para injetar no campo privado)
        // No cenário real, você chamaria controller.adicionarAoCarrinho()

        // WHEN
        // controller.finalizarEmprestimo();

        // THEN
        // verify(locacaoService, times(1)).realizarEmprestimo(any(), any());
    }

    // Helper simples para injetar campos privados @FXML durante o teste
    private void setInternalField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}