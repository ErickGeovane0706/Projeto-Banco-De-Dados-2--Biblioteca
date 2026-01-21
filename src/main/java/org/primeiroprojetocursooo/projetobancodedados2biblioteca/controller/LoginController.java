/*
 * =============================================================================
 * CLASSE: LoginController
 * DESCRICAO: Ponto de entrada do sistema. Responsável pela autenticação de
 * usuários e transição para o ambiente principal (MainView).
 * -----------------------------------------------------------------------------
 * PRINCIPAIS RECURSOS:
 * - Validação de credenciais via UsuarioService.
 * - Integração FXMLLoader + ApplicationContext (Spring) para injeção de beans.
 * - Gerenciamento de troca de Stages e Scenes do JavaFX.
 * - Passagem de estado (Usuário Logado) para o próximo Controller.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 00/00/2026 | [XXXXXXXXX]       |
 * |                  |
 * =============================================================================
 */


package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.UsuarioService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;
    private final ApplicationContext context; // INJEÇÃO NECESSÁRIA

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblErro;
    @FXML private Button btnLogin; // Precisamos do botão para pegar a janela atual

    @FXML
    public void handleLogin() {
        try {
            String email = txtEmail.getText();
            String senha = txtSenha.getText();

            Usuario usuario = usuarioService.autenticar(email, senha);

            lblErro.setVisible(false);
            System.out.println("Login sucesso! Carregando tela principal...");

            carregarTelaPrincipal(usuario); // CHAMA A NOVA TELA

        } catch (IllegalArgumentException e) {
            lblErro.setText(e.getMessage());
            lblErro.setVisible(true);
        } catch (Exception e) {
            lblErro.setText("Erro crítico.");
            e.printStackTrace();
        }
    }

    private void carregarTelaPrincipal(Usuario usuario) {
        try {
            // 1. Carrega o FXML da tela principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));

            // 2. Diz para o JavaFX usar o Spring para criar o controller
            loader.setControllerFactory(context::getBean);

            Parent root = loader.load();

            // 3. Passa o nome do usuário para o MainController
            MainController controller = loader.getController();
            controller.setUsuarioLogado(usuario.getNome());

            // 4. Troca a cena na janela atual
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600)); // Tamanho maior
            stage.centerOnScreen();
            stage.setTitle("Biblioteca - Sistema de Gestão");

        } catch (IOException e) {
            e.printStackTrace();
            lblErro.setText("Erro ao carregar tela principal.");
            lblErro.setVisible(true);
        }
    }
}