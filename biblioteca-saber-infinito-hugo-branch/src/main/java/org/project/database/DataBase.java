package org.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private Connection connection = null;
    private static DataBase INSTANCE = null;

    private DataBase() {
        try {
            // Conecta ao SQLite
            connection = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar no banco!");
            e.printStackTrace();
            throw new RuntimeException("Falha na conexão com o banco de dados", e);
        }
    }

    public static DataBase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataBase();
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        try {
            if (this.connection != null) this.connection.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão!");
            throw new RuntimeException(e);
        }
    }
}