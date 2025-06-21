package org.project.Aplicacao;


public class EmprestimoLivro {
    boolean isDisponivel;
    String dataEmprestimo;
    float multaCalculo;
    String idMembro;
    Long ISBN;

    public EmprestimoLivro(){
        this.isDisponivel=isDisponivel;
        this.dataEmprestimo=dataEmprestimo;
        this.multaCalculo=multaCalculo;
        this.idMembro=idMembro;
        this.ISBN=ISBN;
    }

    public String getDataEmprestimo() {
        return dataEmprestimo;
    }
}
