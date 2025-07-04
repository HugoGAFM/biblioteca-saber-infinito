package org.project.DAO;

import org.project.Aplicacao.Membro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembroDAO {
    private Connection con;
    public MembroDAO(Connection con) {
        this.con = con;
    }

    public void cadastrarMembro(Membro membro){
        String sql = "INSERT INTO membro (nome, telefone, email) VALUES (?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1,membro.getNome());
            stmt.setString(2, membro.getTelefone());
            stmt.setString(3, membro.getEmail());
            stmt.executeUpdate();
            System.out.println("Membro cadastrado com sucesso.");
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar usuario..." + e.getMessage());
        }
    }

    public boolean verficarDependencias (int idMembro){
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
                membros.add(membro);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar membros: " + e.getMessage());
        }
        return membros;
    }
}
