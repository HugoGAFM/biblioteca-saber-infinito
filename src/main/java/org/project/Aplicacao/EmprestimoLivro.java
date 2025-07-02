package org.project.Aplicacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import org.project.database.DataBase;

public class EmprestimoLivro {
    private static int contadorEmprestimos = 0;
    Scanner scanner = new Scanner(System.in);

    int idEmprestimo;
    boolean isDisponivel;
    Date dataEmprestimo;
    float multaCalculo;
    int idMembro;
    Long isbn;

    public EmprestimoLivro(Date dataEmprestimo, float multaCalculo, int idMembro, Long isbn) {
        this.idEmprestimo = ++contadorEmprestimos;
        this.isDisponivel = true;
        this.dataEmprestimo = dataEmprestimo;
        this.multaCalculo = multaCalculo;
        this.idMembro = idMembro;
        this.isbn = isbn;
    }

    //Getters
    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public boolean getIsDisponivel() {
        return isDisponivel;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public float getMultaCalculo() {
        return multaCalculo;
    }

    public int getIdMembro() {
        return idMembro;
    }

    public Long getIsbn() {
        return isbn;
    }
}




