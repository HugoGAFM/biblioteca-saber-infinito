package org.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.project.DAO.MembroDAO;
import org.project.Aplicacao.Membro;

public class CadastroMembroController {
    @FXML private TextField nomeField, telefoneField, emailField;

    private MembroDAO membroDAO;

    public void setMembroDAO(MembroDAO dao) {
        this.membroDAO = dao;
    }

    @FXML
    public void handleSalvar() {
        try {
            Membro membro = new Membro(
                    nomeField.getText(),
                    telefoneField.getText(),
                    emailField.getText()
            );
            membroDAO.cadastrarMembro(membro);
            showMessage("Membro cadastrado com sucesso!");
            limparCampos();
        } catch (Exception e) {
            showMessage("Erro: " + e.getMessage());
        }
    }

    private void limparCampos() {
        nomeField.clear();
        telefoneField.clear();
        emailField.clear();
    }

    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
