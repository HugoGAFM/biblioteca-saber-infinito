package org.project.DAO;

import org.project.Aplicacao.EmprestimoLivro;
import org.project.database.DataBase;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class EmprestimoDAO {
    private Connection con;
    public EmprestimoDAO(Connection con) {
        this.con = con;
    }
    public void registrarEmprestimo(EmprestimoLivro emprestimoLivro) {

    }

    public void registrarDevolucao(EmprestimoLivro emprestimoLivro) {

    }

    public void consultarEmprestimo(EmprestimoLivro emprestimoLivro) {

    }
    public void consultarEmprestimoAtrasado(EmprestimoLivro emprestimoLivro) {

    }
}
