/*
 * =============================================================================
 * CLASSE: MainController
 * DESCRICAO: Controller principal do Dashboard. Gerencia o menu de navegação
 * e a troca dinâmica de telas (Views) no centro do layout.
 * -----------------------------------------------------------------------------
 * PRINCIPAIS RECURSOS:
 * - Troca dinâmica de conteúdo via BorderPane (setCenter).
 * - Centralização da lógica de navegação FXML com suporte ao Spring Context.
 * - Gestão de estado simples (exibição do nome do usuário logado).
 * - Controle de encerramento de sessão (Logout).
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 30/01/2026 | Erivan Barros    | Estruturação da navegação principal e BorderPane.
 * |                  |
 * =============================================================================
 */


package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MainController {

    private final ApplicationContext context; // Para carregar outras telas usando Spring

    @FXML private Label lblBemVindo;
    @FXML private BorderPane painelPrincipal; // Referência ao layout principal

    public void setUsuarioLogado(String nome) {
        lblBemVindo.setText("Olá, " + nome);
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    // --- MÉTODOS DE NAVEGAÇÃO ---

    @FXML
    public void abrirTelaLivros() {
        carregarTela("/fxml/livros.fxml");
    }
    @FXML
    public void abrirTelaLeitores() {
        // Apontamos para usuarios.fxml pois Leitor = Usuario no seu sistema
        carregarTela("/fxml/usuarios.fxml");
    }
    @FXML
    public void abrirTelaEmprestimos() {
        carregarTela("/fxml/emprestimos.fxml");
    }
    @FXML
    public void abrirTelaFaturamento() {
        carregarTela("/fxml/faturamento.fxml");
    }

    // Método genérico para trocar o CENTRO da tela
    private void carregarTela(String caminhoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));
            loader.setControllerFactory(context::getBean); // Garante injeção de dependência
            Parent novaTela = loader.load();

            // Troca o conteúdo do centro
            painelPrincipal.setCenter(novaTela);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar tela: " + caminhoFxml);
        }
    }
}