package org.project.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.project.database.FilesUtil.loadTextFile;

public class BDconnection {
    public static void iniciar() {
        try (
                Connection con = DataBase.getInstance().getConnection();
                Statement statement = con.createStatement();
        ) {
            statement.setQueryTimeout(30);

            // Corrigido: acesso à classe FilesUtil
            String sql = FilesUtil.loadTextFile("tabelasBiblioteca.sql");
            String[] comandos = sql.split(";");

            for (String comando : comandos) {
                comando = comando.trim();
                if (!comando.isEmpty()) {
                    statement.execute(comando + ";");
                }
            }

            // Corrigido: informe o nome da tabela criada no SQL
            ResultSet rs = statement.executeQuery("SELECT * FROM livro"); // ou "membro", etc.

            // Corrigido: garanta que essas colunas existem
            while (rs.next()) {
                System.out.println("ISBN = " + rs.getLong("ISBN"));
                System.out.println("Título = " + rs.getString("titulo"));
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection() {
        return DataBase.getInstance().getConnection();
    }
}
