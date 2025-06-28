package org.project;

import org.project.Aplicacao.Livro;
import org.project.DAO.LivroDAO;
import org.project.database.BDconnection;
import org.project.database.DataBase;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BDconnection.iniciar();

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
                case 1:
                    scanner.nextLine(); // <-- nao esqueçam de fazer isso pra limpar o buffer
                    System.out.print("Digite o ISBN: ");
                    String isbn = scanner.nextLine();

                    System.out.print("Digite o título: ");
                    String titulo = scanner.nextLine();

                    System.out.print("Digite o autor: ");
                    String autor = scanner.nextLine();

                    System.out.print("Digite a data de publicação (AAAA-MM-DD): ");
                    String dataStr = scanner.nextLine();
                    LocalDate dataPublicacao;

                    try {
                        dataPublicacao = LocalDate.parse(dataStr);
                    } catch (DateTimeParseException e) {
                        System.out.println("Data inválida! Use o formato AAAA-MM-DD.");
                        break;
                    }

                    System.out.print("Digite a quantidade de cópias: ");
                    int copias = scanner.nextInt();

                    Livro livro = new Livro(isbn, titulo, autor, dataPublicacao, copias);
                    LivroDAO livroDAO = new LivroDAO(BDconnection.getConnection());
                    livroDAO.inserirLivro(livro);
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