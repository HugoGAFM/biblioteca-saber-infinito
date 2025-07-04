package org.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.project.DAO.LivroDAO;
import org.project.Aplicacao.Livro;

import java.time.LocalDate;

public class CadastroLivroController {
    @FXML private TextField isbnField, tituloField, autorField;
    @FXML private DatePicker dataPicker;
    @FXML private Spinner<Integer> copiasSpinner;

    private LivroDAO livroDAO;

    public void setLivroDAO(LivroDAO dao) {
        this.livroDAO = dao;
        copiasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
    }

    @FXML
    public void handleSalvar() {
        try {
            Livro livro = new Livro(
                    isbnField.getText(),
                    tituloField.getText(),
                    autorField.getText(),
                    dataPicker.getValue(),
                    copiasSpinner.getValue()
            );
            livroDAO.inserirLivro(livro);
            showMessage("Livro cadastrado com sucesso!");
            limparCampos();
        } catch (Exception e) {
            showMessage("Erro: " + e.getMessage());
        }
    }

    private void limparCampos() {
        isbnField.clear();
        tituloField.clear();
        autorField.clear();
        dataPicker.setValue(null);
        copiasSpinner.getValueFactory().setValue(1);
    }

    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
