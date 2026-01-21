/*
 * =============================================================================
 * CLASSE: EmprestimoController
 * DESCRICAO: Controle da View de Empréstimos (JavaFX).
 * Gerencia Carrinho, Histórico de Locações e Regras de Devolução.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 29/01/2026 | [Erivan Barros]       | Implementação inicial e integração com Services.
 * 30/01/2026 | [Erivan Barros]      | Adicionado cálculo de multa na devolução.
 * =============================================================================
 */

package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Livro;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Locacao;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.entity.Usuario;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.LivroService;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.LocacaoService;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.PagamentoService;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.UsuarioService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmprestimoController {

    private final PagamentoService pagamentoService;
    private final LocacaoService locacaoService;
    private final UsuarioService usuarioService;
    private final LivroService livroService;

    // --- ESQUERDA: Formulário ---
    @FXML private ComboBox<Usuario> cbUsuarios;
    @FXML private ComboBox<Livro> cbLivros;
    @FXML private TableView<Livro> tabelaCarrinho;
    @FXML private TableColumn<Livro, String> colCarrinhoTitulo;
    @FXML private Label lblMensagemErro;

    // --- DIREITA: Histórico ---
    @FXML private TableView<Locacao> tabelaLocacoes;
    @FXML private TableColumn<Locacao, Integer> colId;
    @FXML private TableColumn<Locacao, String> colUsuario;
    @FXML private TableColumn<Locacao, LocalDate> colDataLocacao;
    @FXML private TableColumn<Locacao, LocalDate> colDataPrevista;
    @FXML private TableColumn<Locacao, String> colStatus;
    @FXML private Label lblMensagemSucesso;

    // Listas auxiliares
    private ObservableList<Livro> carrinho = FXCollections.observableArrayList();
    private ObservableList<Locacao> historicoLocacoes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabelas();
        configurarCombos();
        carregarDados();
    }

    private void configurarTabelas() {
        // Tabela Carrinho
        colCarrinhoTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tabelaCarrinho.setItems(carrinho);

        // Tabela Histórico
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        // Para exibir o nome do usuário que está dentro do objeto Locacao:
        colUsuario.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsuario().getNome()));

        colDataLocacao.setCellValueFactory(new PropertyValueFactory<>("dataLocacao"));
        colDataPrevista.setCellValueFactory(new PropertyValueFactory<>("dataDevolucaoPrevista"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabelaLocacoes.setItems(historicoLocacoes);
    }

    private void configurarCombos() {
        // Faz o ComboBox mostrar apenas o Nome do Usuário
        cbUsuarios.setConverter(new StringConverter<Usuario>() {
            @Override public String toString(Usuario u) { return u != null ? u.getNome() : ""; }
            @Override public Usuario fromString(String string) { return null; }
        });

        // Faz o ComboBox mostrar apenas o Título do Livro
        cbLivros.setConverter(new StringConverter<Livro>() {
            @Override public String toString(Livro l) { return l != null ? l.getTitulo() : ""; }
            @Override public Livro fromString(String string) { return null; }
        });
    }

    @FXML
    public void carregarDados() {
        // 1. Carrega Usuários
        cbUsuarios.setItems(FXCollections.observableArrayList(usuarioService.listarTodos()));

        // 2. Carrega APENAS Livros Disponíveis
        cbLivros.setItems(FXCollections.observableArrayList(livroService.listarDisponiveis()));

        // 3. Carrega Histórico de Locações
        historicoLocacoes.clear();
        historicoLocacoes.addAll(locacaoService.listarTodas());
    }

    @FXML
    public void adicionarAoCarrinho() {
        Livro livroSelecionado = cbLivros.getValue();
        if (livroSelecionado != null) {
            if (!carrinho.contains(livroSelecionado)) {
                carrinho.add(livroSelecionado);
                cbLivros.getSelectionModel().clearSelection();
                lblMensagemErro.setText("");
            } else {
                lblMensagemErro.setText("Livro já está na lista.");
            }
        }
    }

    @FXML
    public void removerDoCarrinho() {
        Livro selecionado = tabelaCarrinho.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            carrinho.remove(selecionado);
        }
    }

    @FXML
    public void finalizarEmprestimo() {
        Usuario usuario = cbUsuarios.getValue();
        if (usuario == null) {
            lblMensagemErro.setText("Selecione um usuário.");
            return;
        }
        if (carrinho.isEmpty()) {
            lblMensagemErro.setText("Adicione pelo menos um livro.");
            return;
        }

        try {
            // Converte ObservableList para List normal
            List<Livro> livrosParaEmprestar = new ArrayList<>(carrinho);

            locacaoService.realizarEmprestimo(usuario, livrosParaEmprestar);

            // Sucesso
            lblMensagemSucesso.setText("Empréstimo realizado com sucesso!");
            lblMensagemErro.setText("");

            // Limpa tudo e recarrega
            carrinho.clear();
            cbUsuarios.getSelectionModel().clearSelection();
            carregarDados(); // Atualiza a lista de disponíveis (os livros emprestados vão sumir do combo)

        } catch (Exception e) {
            e.printStackTrace();
            lblMensagemErro.setText("Erro: " + e.getMessage());
        }
    }

    public void realizarDevolucao() {
        Locacao locacao = tabelaLocacoes.getSelectionModel().getSelectedItem();

        if (locacao == null) {
            lblMensagemErro.setText("Selecione uma locação na tabela à direita.");
            return;
        }

        if (locacao.getDataDevolucaoReal() != null) {
            lblMensagemErro.setText("Esta locação já foi devolvida e paga.");
            return;
        }

        try {
            // A. Calcula o valor a pagar usando a lógica que já está na sua Entidade Locacao
            Double valorAPagar = locacao.getValorTotal();

            // B. Mostra um Pop-up (Alert) perguntando se confirma o pagamento
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Devolução");
            alert.setHeaderText("Valor a Pagar: R$ " + String.format("%.2f", valorAPagar));
            alert.setContentText("Deseja confirmar o pagamento e devolver os livros?");

            Optional<ButtonType> result = alert.showAndWait();

            // Se o usuário clicou em OK
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // 1. Registra o Pagamento no Banco
                pagamentoService.registrarPagamento(locacao);

                // 2. Realiza a Devolução (libera livros)
                locacaoService.realizarDevolucao(locacao);

                lblMensagemSucesso.setText("Devolução realizada e pagamento de R$ " + valorAPagar + " registrado!");
                lblMensagemErro.setText("");

                carregarDados(); // Atualiza a tabela
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblMensagemErro.setText("Erro ao processar devolução: " + e.getMessage());
        }
    }
}