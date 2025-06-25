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

            String sql = loadTextFile(".sql");
            statement.execute(sql);
            /* statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')"); */
            ResultSet rs = statement.executeQuery("select * from tarefa");
            while (rs.next()) {
                System.out.println("name = " + rs.getString("texto"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
