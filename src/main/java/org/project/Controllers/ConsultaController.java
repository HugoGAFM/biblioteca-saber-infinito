package org.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.project.Aplicacao.Membro;
import org.project.DAO.EmprestimoDAO;
import org.project.DAO.MembroDAO;

import java.util.List;

public class ConsultaController {
    @FXML private ComboBox<Membro> membroBox;
    @FXML private TextArea resultadoArea;

    private EmprestimoDAO emprestimoDAO;
    private MembroDAO membroDAO;

    private Membro membroAtual;

    public void setEmprestimoDAO(EmprestimoDAO dao, MembroDAO mdao) {
        this.emprestimoDAO = dao;
        this.membroDAO = mdao;
        CarregarMembros();
    }

    @FXML
    public void consultarEmprestimos() {
        try {
            membroAtual = membroBox.getSelectionModel().getSelectedItem();
            String resultado = emprestimoDAO.consultarEmprestimoFormatado(membroAtual.getIdMembro());
            resultadoArea.setText(resultado);
        } catch (Exception e) {
            resultadoArea.setText("ID inválido ou erro na consulta.");
        }
    }

    @FXML
    public void consultarMultas() {
        String resultado = emprestimoDAO.consultarMultaFormatado();
        resultadoArea.setText(resultado);
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