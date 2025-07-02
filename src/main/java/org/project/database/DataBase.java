package org.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private Connection connection;

    private static DataBase INSTANCE = null;

    private DataBase(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
        } catch (SQLException e){
            System.err.println("Puts :/ deu erro ao criar o arquivo !");
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar no banco", e);

        }
    }

    public Connection getConnection(){return this.connection;}

    public void closeConnection(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.err.println("Deu erro ao fechar a conexão !");
            throw new RuntimeException(e);
        }
    }

    public static DataBase getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DataBase();
        }
        return INSTANCE;
    }
}