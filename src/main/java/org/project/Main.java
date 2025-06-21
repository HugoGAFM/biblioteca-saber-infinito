package org.project;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int escolha;

        System.out.println("Bem vindo a biblioteca Saber Infinito");

        do{
            System.out.println("O que deja fazer:" +
                    "\n1) Cadastrar livros." +
                    "\n2) Cadastrar membro." +
                    "\n3) Fazer emprestimo." +
                    "\n4) Devolver livro." +
                    "\n0) Sair.");
            escolha = scanner.nextInt();

            switch (escolha){
                case 1://cadastro de livro
                    break;
                case 2://cadastro de mebros
                    break;
                case 3://fazer emprestimo
                    break;
                case 4://devolver livro
                    break;
                default:
                System.out.println("Saindo...");
            }

        }while (escolha!=0);
        

    }
}