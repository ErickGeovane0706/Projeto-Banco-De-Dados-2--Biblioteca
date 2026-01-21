/*
 * =============================================================================
 * CLASSE: LivroController
 * DESCRICAO: Gerencia o ciclo de vida dos livros na interface (CRUD).
 * Responsável pelo cadastro, listagem, exclusão e atualização do acervo
 * bibliográfico no sistema.
 * -----------------------------------------------------------------------------
 * PRINCIPAIS RECURSOS:
 * - Vinculação de colunas TableView via PropertyValueFactory.
 * - Sincronização de ObservableList com o banco de dados via LivroService.
 * - Tratamento visual de mensagens de erro/sucesso (Feedback ao usuário).
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 31/01/2026 | [Erivan Barros]       | Implementação do CRUD e binding da tabela.
 * |                  |
 * =============================================================================
 */

package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Livro;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.LivroService;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService; // Injeção do seu serviço pronto

    // --- Componentes do FXML ---
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtEditora;
    @FXML private Label lblMensagem;

    @FXML private TableView<Livro> tabelaLivros;
    @FXML private TableColumn<Livro, Integer> colId;
    @FXML private TableColumn<Livro, String> colTitulo;
    @FXML private TableColumn<Livro, String> colAutor;
    @FXML private TableColumn<Livro, String> colIsbn;
    @FXML private TableColumn<Livro, String> colStatus;

    // Lista que o JavaFX observa para atualizar a tabela
    private ObservableList<Livro> listaObservavel = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Configurar as colunas da tabela para ler os atributos da classe Livro
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // 2. Carregar dados do banco
        carregarDados();
    }

    private void carregarDados() {
        listaObservavel.clear();
        List<Livro> livrosDoBanco = livroService.listarTodos();
        listaObservavel.addAll(livrosDoBanco);
        tabelaLivros.setItems(listaObservavel);
    }

    @FXML
    public void salvarLivro() {
        try {
            // Cria o objeto usando o Builder do Lombok (como na sua entidade)
            Livro livro = new Livro();
            livro.setTitulo(txtTitulo.getText());
            livro.setAutor(txtAutor.getText());
            livro.setIsbn(txtIsbn.getText());
            livro.setEditora(txtEditora.getText());

            // O Service já cuida de colocar Status DISPONIVEL se for novo
            livroService.salvar(livro);

            lblMensagem.setText("Livro salvo com sucesso!");
            lblMensagem.setStyle("-fx-text-fill: green;");

            limparCampos();
            carregarDados(); // Atualiza a tabela

        } catch (Exception e) {
            e.printStackTrace();
            lblMensagem.setText("Erro ao salvar: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void deletarLivro() {
        Livro selecionado = tabelaLivros.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            livroService.deletar(selecionado.getId());
            carregarDados();
            lblMensagem.setText("Livro excluído.");
        } else {
            lblMensagem.setText("Selecione um livro na tabela para excluir.");
        }
    }

    @FXML
    public void limparCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        txtIsbn.clear();
        txtEditora.clear();
        lblMensagem.setText("");
    }
}