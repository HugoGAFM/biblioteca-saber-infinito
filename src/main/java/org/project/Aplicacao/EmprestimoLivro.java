package org.project.Aplicacao;
import java.util.Date;

public class EmprestimoLivro {
    int idEmprestimo;
    boolean isDisponivel;
    Date dataEmprestimo;
    Date dataDevolucao;
    float multaCalculo;
    int idMembro;
    Long isbn;

    public EmprestimoLivro(){
        this.idEmprestimo = idEmprestimo;
        this.isDisponivel = isDisponivel;
        this.dataEmprestimo = dataEmprestimo;
        this.multaCalculo = multaCalculo;
        this.idMembro = idMembro;
        this.isbn = isbn;
    }

    //Getters
    public int getIdEmprestimo(){
        return idEmprestimo;
    }
    
    public boolean getIsDisponivel(){
        return isDisponivel;
    }

    public Date getDataEmprestimo(){
        return dataEmprestimo;
    }

    public Date getDataDevolucao(){
        return dataDevolucao;
    }

    public float getMultaCalculo(){
        return multaCalculo;
    }

    public int getIdMembro(){
        return idMembro;
    }

    public Long getIsbn(){
        return isbn;
    }

    //Métodos
}
