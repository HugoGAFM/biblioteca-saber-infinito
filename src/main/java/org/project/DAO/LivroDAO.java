
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

    public boolean verificarLivroDisponivel(long isbn) {
        String verificarLivro = "SELECT numCopias FROM livro WHERE ISBN = ?";
        try (PreparedStatement stmt = con.prepareStatement(verificarLivro)) {
            stmt.setLong(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Livro não encontrado.");
                return false;
            }
            int copias = rs.getInt("numCopias");
            if (copias <= 0) {
                System.out.println("📕 Nenhuma cópia disponível deste livro.");
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
