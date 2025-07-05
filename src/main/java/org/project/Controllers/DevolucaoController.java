package org.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.project.DAO.EmprestimoDAO;
import org.project.DAO.MembroDAO;
import org.project.Aplicacao.Membro;

import java.util.List;

public class DevolucaoController {
    @FXML private ComboBox<Membro> membroBox;
    @FXML private ComboBox<Integer> emprestimoBox;

    private EmprestimoDAO emprestimoDAO;
    private MembroDAO membroDAO;

    public void setEmprestimoDAO(EmprestimoDAO dao, MembroDAO mdao) {
        this.emprestimoDAO = dao;
        this.membroDAO = mdao;
        CarregarMembros();
    }

    private Membro membroAtual;

    @FXML
    public void consultarEmprestimos() {
        try {
            membroAtual = membroBox.getSelectionModel().getSelectedItem();
            emprestimoBox.getItems().clear();

            var idEmprestimos = emprestimoDAO.consultarEmprestimosAtivosDoMembro(membroAtual.getIdMembro());
            if (idEmprestimos.isEmpty()) {
                showMessage("Nenhum empréstimo ativo encontrado.");
            } else {
                emprestimoBox.getItems().addAll(idEmprestimos);
                showMessage("Emprestimo encontrado com sucesso.");
            }
        } catch (Exception e) {
            showMessage("ID inválido.");
        }
    }

    @FXML
    public void registrarDevolucao() {
        Integer idEscolhido = emprestimoBox.getValue();
        if (idEscolhido == null || membroAtual == null) {
            showMessage("Selecione um empréstimo.");
            return;
        }

        boolean sucesso = emprestimoDAO.registrarDevolucaoController(idEscolhido);
        if (sucesso) showMessage("Devolução registrada com sucesso!");
        else showMessage("Erro ao registrar devolução.");
    }

    @FXML
    public void CarregarMembros() {
        try {
            List<Membro> resultado = membroDAO.buscarTodos();

            for (Membro membros : resultado) {
                membroBox.getItems().add(membros);
            }

        } catch (Exception e) {
            showMessage("Erro: " + e.getMessage());
        }
    }
    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}