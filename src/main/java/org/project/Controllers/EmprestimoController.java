package org.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.project.Aplicacao.EmprestimoLivro;
import org.project.Aplicacao.Livro;
import org.project.Aplicacao.Membro;
import org.project.DAO.EmprestimoDAO;
import org.project.DAO.LivroDAO;
import org.project.DAO.MembroDAO;

import java.util.Date;
import java.util.List;

public class EmprestimoController {
    @FXML private ComboBox<Membro> membroBox;
    @FXML private ComboBox<Livro> livroBox;

    private EmprestimoDAO emprestimoDAO;
    private MembroDAO membroDAO;
    private LivroDAO livroDAO;

    public void setDAOs(EmprestimoDAO eDao, MembroDAO mDao, LivroDAO lDao) {
        this.emprestimoDAO = eDao;
        this.membroDAO = mDao;
        this.livroDAO = lDao;

        carregarMembros();
        carregarLivros();
    }

    private void carregarMembros() {
        List<Membro> membros = membroDAO.buscarTodos();
        membroBox.getItems().addAll(membros);
    }

    private void carregarLivros() {
        List<Livro> livros = livroDAO.buscarDisponiveis();
        livroBox.getItems().addAll(livros);
    }

    @FXML
    public void registrarEmprestimo() {
        Membro membro = membroBox.getValue();
        Livro livro = livroBox.getValue();

        if (membro == null || livro == null) {
            showMessage("Selecione um membro e um livro.");
            return;
        }

        if (!membroDAO.verficarDependencias(membro.getIdMembro())) {
            showMessage("Membro possui pedências. Empréstimo não permitido.");
            return;
        }

        if (!livroDAO.verificarLivroDisponivel(Long.parseLong(livro.getISBN()))) {
            showMessage("Livro indisponível.");
            return;
        }

        EmprestimoLivro emprestimo = new EmprestimoLivro(new Date(), 0f, membro.getIdMembro(), Long.parseLong(livro.getISBN()));
        emprestimoDAO.registrarEmprestimo(emprestimo);
        showMessage("Empréstimo registrado com sucesso!");
    }
    @FXML
    public void atualizarListas() {
        membroBox.getItems().clear();
        livroBox.getItems().clear();
        carregarMembros();
        carregarLivros();
    }

    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}