package org.project.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.project.database.FilesUtil.loadTextFile;

public class BDconnection {
    public static void iniciar() {
        try {
            Connection con = DataBase.getInstance().getConnection(); // NÃO FECHAR AQUI
            Statement statement = con.createStatement();

            statement.setQueryTimeout(30);

            String sql = FilesUtil.loadTextFile("tabelasBiblioteca.sql");
            String[] comandos = sql.split(";");

            for (String comando : comandos) {
                comando = comando.trim();
                if (!comando.isEmpty()) {
                    statement.execute(comando + ";");
                }
            }

            ResultSet rs = statement.executeQuery("SELECT * FROM livro");

            while (rs.next()) {
                System.out.println("ISBN = " + rs.getLong("ISBN"));
                System.out.println("Título = " + rs.getString("titulo"));
            }

            statement.close(); // Fecha apenas o Statement
            // NÃO fechar a conexão aqui!

        } catch (SQLException | IOException e) {
            e.printStackTrace(System.err);
        }
    }
    public static Connection getConnection() {
        return DataBase.getInstance().getConnection();
    }
}
