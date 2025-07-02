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

            String atualizarMembro = "UPDATE membro SET devendo = 1 WHERE idMembro = ?";
            try (PreparedStatement stmt = con.prepareStatement(atualizarMembro)) {
                stmt.setLong(1, emprestimo.getIdMembro());
                stmt.executeUpdate();
                System.out.println("Membro atualizado: Agora esta devendo.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar empréstimo no banco: " + e.getMessage());
        }
    }

    public void registrarDevolucao(EmprestimoLivro emprestimoLivro) {

    }

    public void consultarEmprestimo(int idConsulta) {
        try (Connection con = DataBase.getInstance().getConnection()) {
            String sql = "SELECT ISBN, dataEmprestimo FROM emprestimoLivro WHERE idMembro = ? AND isDisponivel = 0";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, idConsulta);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    long isbn = rs.getLong("ISBN");
                    Date dataEmprestimo = java.sql.Date.valueOf(rs.getString("dataEmprestimo"));

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dataEmprestimo);
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                    Date dataPrevista = cal.getTime();

                    System.out.println("ISBN: " + isbn);
                    System.out.println("Data do empréstimo: " + dataEmprestimo);
                    System.out.println("Data prevista para devolução: " + dataPrevista);
                } else {
                    System.out.println("Este membro não possui livros emprestados no momento.");
                }

            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar empréstimos: " + e.getMessage());
        }
    }
}
