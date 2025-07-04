package org.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.project.DAO.EmprestimoDAO;

public class ConsultaController {
    @FXML private TextField idConsultaField;
    @FXML private TextArea resultadoArea;

    private EmprestimoDAO emprestimoDAO;

    public void setEmprestimoDAO(EmprestimoDAO dao) {
        this.emprestimoDAO = dao;
    }

    @FXML
    public void consultarEmprestimos() {
        try {
            int id = Integer.parseInt(idConsultaField.getText());
            String resultado = emprestimoDAO.consultarEmprestimoFormatado(id);
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
}