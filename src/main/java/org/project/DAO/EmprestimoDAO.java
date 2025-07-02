package org.project.DAO;

import org.project.Aplicacao.EmprestimoLivro;
import org.project.database.DataBase;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class EmprestimoDAO {
    private Connection con;
    public EmprestimoDAO(Connection con) {
        this.con = con;
    }
    public void registrarEmprestimo(EmprestimoLivro emprestimo){
        Connection con = DataBase.getInstance().getConnection();

        //Gravar no banco
        try(con){
            String sql = "INSERT INTO emprestimoLivro (isDisponivel, dataEmprestimo, multaCalculo, idMembro, ISBN) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setBoolean(1, false);
                stmt.setString(2, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()).toString());
                stmt.setString(3, new java.sql.Date(emprestimo.getDataDevolucao().getTime()).toString());
                stmt.setFloat(4, 0.0f);
                stmt.setInt(5, emprestimo.getIdMembro());
                stmt.setLong(6, emprestimo.getIsbn());
                stmt.executeUpdate();
                System.out.println("Empréstimo salvo com sucesso no banco de dados!");
            }

            String atualizarEstoque = "UPDATE livro SET numCopias = numCopias - 1 WHERE ISBN = ?";
            try (PreparedStatement stmt = con.prepareStatement(atualizarEstoque)) {
                stmt.setLong(1, emprestimo.getIsbn());
                stmt.executeUpdate();
                System.out.println("📦 Estoque atualizado: 1 cópia emprestada.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar empréstimo no banco: " + e.getMessage());
        }
    }

    public void registrarDevolucao(EmprestimoLivro emprestimoLivro) {

    }

    public void consultarEmprestimo(EmprestimoLivro emprestimoLivro) {

    }
    public void consultarEmprestimoAtrasado(EmprestimoLivro emprestimoLivro) {

    }
}
