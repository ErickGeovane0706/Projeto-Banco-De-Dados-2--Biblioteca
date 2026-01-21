/*
 * =============================================================================
 * CLASSE: FaturamentoControllerTest
 * DESCRICAO: Testes Unitários para o Controle de Faturamento.
 * Valida a lógica de filtragem por período e integração com PagamentoService.
 * Verifica o comportamento dos campos de data e formatação de valores.
 * -----------------------------------------------------------------------------
 * HISTÓRICO DE MANUTENÇÃO:
 * DATA       | AUTOR            | DESCRIÇÃO
 * -----------|------------------|----------------------------------------------
 * 31/01/2026 | Erivan Barros  | Implementação dos testes de filtragem e mocks.
 * =============================================================================
 */

package org.primeiroprojetocursooo.projetobancodedados2biblioteca.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primeiroprojetocursooo.projetobancodedados2biblioteca.services.PagamentoService;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class FaturamentoControllerTest {

    @Mock
    private PagamentoService pagamentoService;

    @InjectMocks
    private FaturamentoController controller;

    // Elementos de UI que precisam ser instanciados manualmente
    private DatePicker dpInicio;
    private DatePicker dpFim;
    private Label lblValorFaturamento;

    @BeforeAll
    static void initJavaFX() {
        if (!Platform.isFxApplicationThread()) {
            try {
                Platform.startup(() -> {});
            } catch (IllegalStateException e) { /* Já inicializado */ }
        }
    }

    @BeforeEach
    void setup() throws Exception {
        dpInicio = new DatePicker();
        dpFim = new DatePicker();
        lblValorFaturamento = new Label();

        // Injeção manual nos campos @FXML privados
        setField(controller, "dpInicio", dpInicio);
        setField(controller, "dpFim", dpFim);
        setField(controller, "lblValorFaturamento", lblValorFaturamento);
    }

    @Test
    void deveExibirMensagemErroQuandoDatasForemNulas() {
        // GIVEN: Datas não preenchidas
        dpInicio.setValue(null);
        dpFim.setValue(null);

        // WHEN
        controller.filtrarFaturamento();

        // THEN
        assertEquals("Selecione as datas!", lblValorFaturamento.getText());
        verifyNoInteractions(pagamentoService);
    }

    @Test
    void deveExibirErroSeDataInicioForDepoisDeDataFim() {
        // GIVEN: Data início > Data fim
        dpInicio.setValue(LocalDate.of(2026, 01, 31));
        dpFim.setValue(LocalDate.of(2026, 01, 01));

        // WHEN
        controller.filtrarFaturamento();

        // THEN
        assertEquals("Data inválida!", lblValorFaturamento.getText());
    }

    @Test
    void deveFormatarValorCorretamenteQuandoServiceRetornarFaturamento() {
        // GIVEN
        LocalDate inicio = LocalDate.of(2026, 01, 01);
        LocalDate fim = LocalDate.of(2026, 01, 31);
        dpInicio.setValue(inicio);
        dpFim.setValue(fim);

        when(pagamentoService.obterFaturamentoPorPeriodo(inicio, fim)).thenReturn(1550.50);

        // WHEN
        controller.filtrarFaturamento();

        // THEN
        assertEquals("R$ 1550,50", lblValorFaturamento.getText());
        verify(pagamentoService, times(1)).obterFaturamentoPorPeriodo(inicio, fim);
    }

    @Test
    void deveLimparCamposAoResetarFiltro() {
        // GIVEN
        dpInicio.setValue(LocalDate.now());
        lblValorFaturamento.setText("R$ 100,00");

        // WHEN
        controller.limparFiltro();

        // THEN
        assertNull(dpInicio.getValue());
        assertEquals("R$ 0,00", lblValorFaturamento.getText());
    }

    // Helper para acessar os campos @FXML privados
    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
