package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.PagamentoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@RequiredArgsConstructor
public class FaturamentoController {

    private final PagamentoService pagamentoService;

    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private Label lblValorFaturamento;

    @FXML
    public void initialize() {
        // 1. Configura o formato Brasileiro (DD/MM/AAAA)
        String pattern = "dd/MM/yyyy";
        StringConverter<LocalDate> converter = new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (DateTimeParseException e) {
                        return null; // Se digitar data errada, retorna nulo
                    }
                }
                return null;
            }
        };

        dpInicio.setConverter(converter);
        dpFim.setConverter(converter);

        // 2. TRUQUE DO ENTER: Força salvar a data quando clica fora do campo
        configurarCommitAutomatico(dpInicio);
        configurarCommitAutomatico(dpFim);

        lblValorFaturamento.setText("R$ 0,00");
    }

    // Método auxiliar para não precisar apertar ENTER
    private void configurarCommitAutomatico(DatePicker datePicker) {
        datePicker.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Se perdeu o foco (clicou fora)
                try {
                    String textoDigitado = datePicker.getEditor().getText();
                    LocalDate data = datePicker.getConverter().fromString(textoDigitado);
                    datePicker.setValue(data);
                } catch (Exception e) {
                    // Ignora erros de digitação ao sair
                }
            }
        });
    }

    @FXML
    public void filtrarFaturamento() {
        // Antes de pegar o valor, forçamos o commit do texto atual para garantir
        // Isso resolve caso o listener de foco ainda não tenha disparado
        commitEditorText(dpInicio);
        commitEditorText(dpFim);

        LocalDate inicio = dpInicio.getValue();
        LocalDate fim = dpFim.getValue();

        if (inicio == null || fim == null) {
            lblValorFaturamento.setText("Selecione as datas!");
            if(inicio == null) dpInicio.setStyle("-fx-border-color: red;");
            if(fim == null) dpFim.setStyle("-fx-border-color: red;");
            return;
        }

        dpInicio.setStyle("");
        dpFim.setStyle("");

        if (inicio.isAfter(fim)) {
            lblValorFaturamento.setText("Data inválida!");
            return;
        }

        Double valor = pagamentoService.obterFaturamentoPorPeriodo(inicio, fim);
        if (valor == null) valor = 0.0;

        lblValorFaturamento.setText(String.format("R$ %.2f", valor));
    }

    // Garante que o texto vire data antes de filtrar
    private void commitEditorText(DatePicker datePicker) {
        if (datePicker.getEditor().getText() != null && !datePicker.getEditor().getText().isEmpty()) {
            try {
                LocalDate data = datePicker.getConverter().fromString(datePicker.getEditor().getText());
                datePicker.setValue(data);
            } catch (Exception ignored) {}
        }
    }

    @FXML
    public void limparFiltro() {
        dpInicio.setValue(null);
        dpInicio.getEditor().setText(""); // Limpa o texto visual também
        dpFim.setValue(null);
        dpFim.getEditor().setText("");

        dpInicio.setStyle("");
        dpFim.setStyle("");
        lblValorFaturamento.setText("R$ 0,00");
    }
}