package org.project.ui;

import java.util.Scanner;

public class TerminalBibliotecaUI implements BibliotecaUI{
    private BibliotecaUI ui;

    public TerminalBibliotecaUI(BibliotecaUI bibliotecaUI){
        this.ui = bibliotecaUI;
        this.escolhaAcao();
    }

    @Override
    public int escolhaAcao() {
        Scanner scanner=new Scanner(System.in);
        int escolha;

        do{
            System.out.println("O que deja fazer:" +
                    "\n1) Cadastrar livros." +
                    "\n2) Cadastrar membro." +
                    "\n3) Buscar livros." +
                    "\n4) Fazer emprestimo." +
                    "\n5) Devolver livro." +
                    "\n0) Sair.");
            escolha = scanner.nextInt();

            switch (escolha){
                case 1://cadastro de livro
                    break;
                case 2://cadastro de mebros
                    break;
                case 3://buscar livro
                    break;
                case 4://fazer emprestimo
                    break;
                case 5://devolver livro
                    break;
                default:
                    System.out.println("Saindo...");
            }

        }while (escolha!=0);

        return 0;
    }

    @Override
    public void criarMembro() {

    }

    @Override
    public void buscaLivro() {

    }

    @Override
    public void fazerEmprestimo() {

    }

    @Override
    public void devolverLivro() {

    }
}
