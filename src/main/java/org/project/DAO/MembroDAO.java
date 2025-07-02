package org.project.DAO;

import java.sql.Connection;

public class MembroDAO {
    private Connection con;
    public MembroDAO(Connection con) {
        this.con = con;
    }
}
