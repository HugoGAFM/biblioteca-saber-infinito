package org.project.DAO;

import org.project.Aplicacao.Membro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembroDAO {
    private Connection con;
    public MembroDAO(Connection con) {
        this.con = con;
    }

    public void cadastrarMembro(Membro membro) {
        String sql = "INSERT INTO membro (nome, telefone, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, membro.getNome());
            stmt.setString(2, membro.getTelefone());
            stmt.setString(3, membro.getEmail());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                membro.setIdMembro(id);
            }

            System.out.println("Membro cadastrado com ID: " + membro.getIdMembro());

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar membro: " + e.getMessage());
        }
    }

    public boolean verificarDependencias (int idMembro){
        String verificarPendencia = "SELECT devendo FROM membro WHERE idMembro = ?";
        try (
                PreparedStatement stmt = con.prepareStatement(verificarPendencia)) {
            stmt.setInt(1, idMembro);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Membro não encontrado.");
                return false;
            }
            if (rs.getBoolean("devendo")) {
                System.out.println("Membro possui pendências. Empréstimo não permitido.");
                return false;
            }
            else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Membro> buscarTodos() {
        List<Membro> membros = new ArrayList<>();
        String sql = "SELECT idMembro, nome, telefone, email FROM membro";
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Membro membro = new Membro(
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );
                membro.setIdMembro(rs.getInt("idMembro"));
                membros.add(membro);
                System.out.println("Membro carregado: " + membro.getNome() + " (ID: " + membro.getIdMembro() + ")");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar membros: " + e.getMessage());
        }
        return membros;
    }
}
