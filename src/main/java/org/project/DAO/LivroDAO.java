package org.project.DAO;


import org.project.Aplicacao.Livro;
import java.sql.*;

public class LivroDAO {
    private Connection con;

    public LivroDAO(Connection con) {
        this.con = con;
    }

    public void inserirLivro(Livro livro) {
        String sql = "INSERT INTO livro (ISBN, titulo, autor, dataPublicacao, numCopias) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, livro.getISBN());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getAutor());
            stmt.setString(4, livro.getDataPublicacao().toString());
            stmt.setInt(5, livro.getnumCopias());
            stmt.executeUpdate();
            System.out.println("Livro cadastrado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }
}