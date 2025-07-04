package org.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.project.DAO.EmprestimoDAO;

public class DevolucaoController {
    @FXML private TextField idMembroField;
    @FXML private ComboBox<Integer> emprestimoBox;

    private EmprestimoDAO emprestimoDAO;

    public void setEmprestimoDAO(EmprestimoDAO dao) {
        this.emprestimoDAO = dao;
    }

    private int membroAtual = -1;

    @FXML
    public void consultarEmprestimos() {
        try {
            membroAtual = Integer.parseInt(idMembroField.getText());
            emprestimoBox.getItems().clear();

            var idEmprestimos = emprestimoDAO.consultarEmprestimosAtivosDoMembro(membroAtual);
            if (idEmprestimos.isEmpty()) {
                showMessage("Nenhum empréstimo ativo encontrado.");
            } else {
                emprestimoBox.getItems().addAll(idEmprestimos);
            }
        } catch (Exception e) {
            showMessage("ID inválido.");
        }
    }

    @FXML
    public void registrarDevolucao() {
        Integer idEscolhido = emprestimoBox.getValue();
        if (idEscolhido == null || membroAtual == -1) {
            showMessage("Selecione um empréstimo.");
            return;
        }

        boolean sucesso = emprestimoDAO.registrarDevolucaoController(idEscolhido);
        if (sucesso) showMessage("Devolução registrada com sucesso!");
        else showMessage("Erro ao registrar devolução.");
    }

    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}