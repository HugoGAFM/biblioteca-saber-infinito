package org.project.Aplicacao;

import java.util.Date;

public class EmprestimoLivro {
    private static int contadorEmprestimos = 0;

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




