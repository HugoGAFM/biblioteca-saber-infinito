package org.project.Aplicacao;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;

public class EmprestimoLivro {
    private static int contadorEmprestimos = 0;
    Scanner scanner = new Scanner(System.in);
    
    int idEmprestimo;
    boolean isDisponivel;
    Date dataEmprestimo;
    Date dataDevolucao;
    float multaCalculo;
    int idMembro;
    Long isbn;

    public EmprestimoLivro(int idEmprestimo, boolean isDisponivel, Date dataEmprestimo, Date dataDevolucao, float multaCalculo, int idMembro, Long isbn){
        this.idEmprestimo = ++contadorEmprestimos;
        this.isDisponivel = true;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
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

    //Setters
    public void setIdEmprestimo(int idEmprestimo) {
    this.idEmprestimo = idEmprestimo;
    }
    
    public void setIsDisponivel(boolean isDisponivel) {
        this.isDisponivel = isDisponivel;
    }
    
    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }
    
    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
    
    public void setMultaCalculo(float multaCalculo) {
        this.multaCalculo = multaCalculo;
    }
    
    public void setIdMembro(int idMembro) {
        this.idMembro = idMembro;
    }
    
    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }


    //Métodos
    public void registrarEmprestimo(){
        System.out.println("REALIZAR EMPRÉSTIMO");
        System.out.print("Digite o ID do membro: ");
        this.idMembro = scanner.nextInt();
        System.out.print("Digite o ISBN: ");
        this.isbn = scanner.nextLong();

        this.dataEmprestimo = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataEmprestimo);
        cal.add(Calendar.DAY_OF_MONTH,7);
        this.dataDevolucao = cal.getTime();
        
        this.isDisponivel = false;

        System.out.println("Empréstimo registrado com sucesso!");
        System.out.println("Data do empréstimo: " + dataEmprestimo);
        System.out.println("Data prevista para devolução: " + dataDevolucao);
    }

    public void registrarDevolucao(){
        System.out.println("REALIZAR DEVOLUÇÃO");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Digite o ID do membro: ");
        int idMembro = Integer.parseInt(scanner.nextLine());
        
        System.out.println("\nLIVROS EMPRESTADOS AO USUÁRIO");
        for(int i=1; i<????; i++){
            System.out.printf("%i. %s", i, );
        }
    }
}
