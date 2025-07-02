package org.project.DAO;

import org.project.Aplicacao.Membro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MembroDAO {
    private Connection con;
    public MembroDAO(Connection con) {
        this.con = con;
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

}
