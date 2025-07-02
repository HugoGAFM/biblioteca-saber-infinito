package org.project.DAO;

import org.project.Aplicacao.Membro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.sql.SQLException;

public class MembroDAO {
    private Connection con;
    public MembroDAO(Connection con){
        this.con = con;
    }

    public void cadastrarMembro(Membro membro){
        String sql = "INSERT INTO membro (nome, telefone, email) VALUES (?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1,membro.getNome());
            stmt.setString(3, membro.getTelefone());
            stmt.setString(2, membro.getEmail());
            stmt.executeUpdate();
            System.out.println("Membro cadastrado com sucesso.");
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar usuario..." + e.getMessage());
        }
    }


}
