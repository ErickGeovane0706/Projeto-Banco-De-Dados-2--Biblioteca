/*
 * =============================================================================
 * CLASSE: UsuarioController
 * DESCRICAO: Gerencia o cadastro e a manutenção de usuários (leitores e admins).
 * Controla o formulário de registro e a visualização de perfis no sistema.
 * -----------------------------------------------------------------------------
 * PRINCIPAIS RECURSOS:
 * - Gerenciamento de ComboBox utilizando Enums (TipoUsuario).
 * - CRUD completo de usuários com feedback visual de mensagens.
 * - Integração com UsuarioService para persistência e regras de negócio.
 * - Tratamento de restrições de exclusão (Ex: usuários com pendências).
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 30/01/2026 | Erivan Barros    | Implementação do CRUD de usuários e Enums.
 * |                  |
 * =============================================================================
 */

package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.enums.TipoUsuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.UsuarioService;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // --- CAMPOS DO FORMULÁRIO ---
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private TextField txtMatricula;
    @FXML private TextField txtTelefone;
    @FXML private ComboBox<TipoUsuario> comboTipo; // ComboBox Tipada com o Enum

    @FXML private Label lblMensagem;

    // --- TABELA ---
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colMatricula;
    @FXML private TableColumn<Usuario, String> colTipo;

    private ObservableList<Usuario> listaObservavel = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Configurar Colunas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // 2. Preencher o ComboBox com as opções do Enum (ALUNO, PROFESSOR, ADMIN)
        comboTipo.setItems(FXCollections.observableArrayList(TipoUsuario.values()));

        // 3. Carregar dados
        carregarDados();
    }

    private void carregarDados() {
        listaObservavel.clear();
        List<Usuario> usuarios = usuarioService.listarTodos();
        listaObservavel.addAll(usuarios);
        tabelaUsuarios.setItems(listaObservavel);
    }

    @FXML
    public void salvarUsuario() {
        try {
            // Validações simples visuais
            if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty() || comboTipo.getValue() == null) {
                lblMensagem.setText("Preencha os campos obrigatórios (*)");
                lblMensagem.setStyle("-fx-text-fill: red;");
                return;
            }

            // Criação do objeto
            Usuario usuario = new Usuario();
            usuario.setNome(txtNome.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setMatricula(txtMatricula.getText());
            usuario.setTelefone(txtTelefone.getText());
            usuario.setTipo(comboTipo.getValue());

            // A senha é obrigatória na sua entidade
            if (txtSenha.getText().isEmpty()) {
                lblMensagem.setText("A senha é obrigatória para novos usuários.");
                return;
            }
            usuario.setSenha(txtSenha.getText()); // O Service vai criptografar isso!

            usuarioService.salvar(usuario);

            lblMensagem.setText("Usuário salvo com sucesso!");
            lblMensagem.setStyle("-fx-text-fill: green;");

            limparCampos();
            carregarDados();

        } catch (Exception e) {
            e.printStackTrace();
            lblMensagem.setText("Erro: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void deletarUsuario() {
        Usuario selecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                usuarioService.deletar(selecionado.getId());
                carregarDados();
                lblMensagem.setText("Usuário excluído.");
                lblMensagem.setStyle("-fx-text-fill: green;");
            } catch (Exception e) {
                lblMensagem.setText("Não é possível excluir usuário com empréstimos ativos.");
                lblMensagem.setStyle("-fx-text-fill: red;");
            }
        } else {
            lblMensagem.setText("Selecione um usuário para excluir.");
        }
    }

    @FXML
    public void limparCampos() {
        txtNome.clear();
        txtEmail.clear();
        txtSenha.clear();
        txtMatricula.clear();
        txtTelefone.clear();
        comboTipo.getSelectionModel().clearSelection();
        lblMensagem.setText("");
    }
}
