package org.project.database;

import java.io.IOException;
import java.sql.*;

import static org.project.database.FilesUtil.loadTextFile;

public class BDconnection {
    public static void iniciar() {
        try (
                // Conexão temporária só para criação de tabelas
                Connection con = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
                Statement statement = con.createStatement()
        ) {
            statement.setQueryTimeout(30);

            String sql = FilesUtil.loadTextFile("tabelasBiblioteca.sql");
            String[] comandos = sql.split(";");

            for (String comando : comandos) {
                comando = comando.trim();
                if (!comando.isEmpty()) {
                    statement.execute(comando + ";");
                }
            }

            System.out.println("\nTabelas carregadas com sucesso. Exibindo livros:");
            ResultSet rs = statement.executeQuery("SELECT * FROM livro");

            while (rs.next()) {
                System.out.println("ISBN = " + rs.getLong("ISBN"));
                System.out.println("Título = " + rs.getString("titulo"));
            }

        } catch (SQLException | IOException e) {
            System.err.println("Erro ao inicializar o banco:");
            e.printStackTrace();
        }
    }

    // Retorna a conexão principal mantida viva
    public static Connection getConnection() {
        return DataBase.getInstance().getConnection();
    }
}
