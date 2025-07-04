package org.project.DAO;

import org.project.Aplicacao.Livro;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                System.out.println("Nenhuma cópia disponível deste livro.");
                return false;
            }
            else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Livro> buscarDisponiveis() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT ISBN, titulo, autor, dataPublicacao, numCopias FROM livro WHERE numCopias > 0";
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("ISBN"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        LocalDate.parse(rs.getString("dataPublicacao")),
                        rs.getInt("numCopias")
                );
                livros.add(livro);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livros disponíveis: " + e.getMessage());
        }
        return livros;
    }
}
