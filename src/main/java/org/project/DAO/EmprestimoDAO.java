package org.project.DAO;

import org.project.Aplicacao.EmprestimoLivro;
import org.project.Aplicacao.Membro;
import org.project.database.DataBase;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EmprestimoDAO {
    private Connection con;

    public EmprestimoDAO(Connection con) {
        this.con = con;
    }
    public void registrarEmprestimo(EmprestimoLivro emprestimo){
        Connection con = DataBase.getInstance().getConnection();

        try {
            String sql = "INSERT INTO emprestimoLivro (isDisponivel, dataEmprestimo, multaCalculo, idMembro, ISBN) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setBoolean(1, false);
                stmt.setString(2, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()).toString());
                stmt.setFloat(3, 0.0f);
                stmt.setInt(4, emprestimo.getIdMembro());
                stmt.setLong(5, emprestimo.getIsbn());
                stmt.executeUpdate();
                System.out.println("Empréstimo salvo com sucesso no banco de dados!");
                emprestimo.setContadorEmprestimo();
            }

            String atualizarEstoque = "UPDATE livro SET numCopias = numCopias - 1 WHERE ISBN = ?";
            try (PreparedStatement stmt = con.prepareStatement(atualizarEstoque)) {
                stmt.setLong(1, emprestimo.getIsbn());
                stmt.executeUpdate();
                System.out.println("O usuário pegou uma cópia de livro.");
            }

            String atualizarMembro = "UPDATE membro SET devendo = 1 WHERE idMembro = ?";
            try (PreparedStatement stmt = con.prepareStatement(atualizarMembro)) {
                stmt.setLong(1, emprestimo.getIdMembro());
                stmt.executeUpdate();
                System.out.println("Membro atualizado: Agora está devendo.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar empréstimo no banco: " + e.getMessage());
        }
    }

    public List<Integer> consultarEmprestimosAtivosDoMembro(int idMembro) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT idEmprestimo FROM emprestimoLivro WHERE idMembro = ? AND isDisponivel = 0";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idMembro);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("idEmprestimo"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar empréstimos ativos: " + e.getMessage());
        }

        return lista;
    }

    public boolean registrarDevolucaoController(int idEmprestimo) {
        try {
            String buscarEmprestimo = "SELECT dataEmprestimo, ISBN, IdMembro FROM emprestimoLivro WHERE idEmprestimo = ?";
            try (PreparedStatement stmtBusca = con.prepareStatement(buscarEmprestimo)) {
                stmtBusca.setInt(1, idEmprestimo);
                ResultSet rs = stmtBusca.executeQuery();

                if (rs.next()) {
                    String dataEmpStr = rs.getString("dataEmprestimo");
                    long isbnLivro = rs.getLong("ISBN");
                    Date dataEmprestimo = java.sql.Date.valueOf(dataEmpStr);
                    int idMembro = rs.getInt("IdMembro");

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dataEmprestimo);
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                    Date dataPrevista = cal.getTime();

                    Date hoje = new Date();
                    long diasAtraso = (hoje.getTime() - dataPrevista.getTime()) / (1000 * 60 * 60 * 24);
                    float multa = diasAtraso > 0 ? diasAtraso * 2.0f : 0.0f;

                    String atualizar = "UPDATE emprestimoLivro SET isDisponivel = 1, dataDevolucao = ?, multaCalculo = ? WHERE idEmprestimo = ?";
                    try (PreparedStatement stmtAtualiza = con.prepareStatement(atualizar)) {
                        stmtAtualiza.setString(1, new java.sql.Date(hoje.getTime()).toString());
                        stmtAtualiza.setFloat(2, multa);
                        stmtAtualiza.setInt(3, idEmprestimo);
                        stmtAtualiza.executeUpdate();
                    }

                    String devolverCopia = "UPDATE livro SET numCopias = numCopias + 1 WHERE ISBN = ?";
                    try (PreparedStatement stmtEstoque = con.prepareStatement(devolverCopia)) {
                        stmtEstoque.setLong(1, isbnLivro);
                        stmtEstoque.executeUpdate();
                    }

                    String atualizarMembro = "UPDATE membro SET devendo = 0 WHERE idMembro = ?";
                    try (PreparedStatement stmt = con.prepareStatement(atualizarMembro)) {
                        stmt.setLong(1, idMembro);
                        stmt.executeUpdate();
                        System.out.println("Membro atualizado: Agora não está devendo.");
                    }

                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao registrar devolução: " + e.getMessage());
        }

        return false;
    }
    public String consultarEmprestimoFormatado(int idMembro) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT ISBN, dataEmprestimo FROM emprestimoLivro WHERE idMembro = ? AND isDisponivel = 0";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idMembro);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sb.append("ISBN: ").append(rs.getLong("ISBN"))
                        .append(" | Data: ").append(rs.getString("dataEmprestimo")).append("\n");
            }

            if (sb.length() == 0) {
                sb.append("Nenhum empréstimo ativo encontrado.");
            }

        } catch (SQLException e) {
            sb.append("Erro ao consultar empréstimos: ").append(e.getMessage());
        }

        return sb.toString();
    }

    public String consultarMultaFormatado() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT idMembro, dataEmprestimo FROM emprestimoLivro WHERE isDisponivel = 0";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idMembro");
                Date dataEmp = java.sql.Date.valueOf(rs.getString("dataEmprestimo"));

                Calendar cal = Calendar.getInstance();
                cal.setTime(dataEmp);
                cal.add(Calendar.DAY_OF_MONTH, 7);
                Date limite = cal.getTime();

                Date hoje = new Date();
                if (hoje.after(limite)) {
                    sb.append("Membro #").append(id).append(" está em atraso.\n");
                }
            }

            if (sb.length() == 0) {
                sb.append("Nenhum membro com multa.");
            }

        } catch (SQLException e) {
            sb.append("Erro ao consultar multas: ").append(e.getMessage());
        }

        return sb.toString();
    }
}



