package org.project;

import org.project.Aplicacao.EmprestimoLivro;
import org.project.Aplicacao.Livro;
import org.project.DAO.EmprestimoDAO;
import org.project.DAO.LivroDAO;
import org.project.DAO.MembroDAO;
import org.project.database.BDconnection;
import org.project.database.DataBase;
import java.util.Calendar;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BDconnection.iniciar();

        Scanner scanner=new Scanner(System.in);
        int escolha;
        LivroDAO livroDAO = new LivroDAO(BDconnection.getConnection());
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO(BDconnection.getConnection());
        MembroDAO membroDAO = new MembroDAO(BDconnection.getConnection());

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

                    livroDAO.inserirLivro(livro);
                    break;
                case 2: //cadastro de membros
                    break;
                case 3: //fazer emprestimo
                    scanner.nextLine(); // <-- nao esqueçam de fazer isso pra limpar o buffer
                    System.out.print("Digite o id do membro: ");
                    int idMembroEmprestimo = scanner.nextInt();

                    System.out.print("Digite o isbn: ");
                    long isbnEmprestimo = scanner.nextLong();

                    if (livroDAO.verificarLivroDisponivel(isbnEmprestimo) && membroDAO.verficarDependencias(idMembroEmprestimo)){
                        int idEmprestimo = 1;
                        boolean isDisponivel = false;
                        Date dataEmprestimo = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dataEmprestimo);
                        cal.add(Calendar.DAY_OF_MONTH,7);
                        Date dataDevolucao = cal.getTime();
                        float multaCalculo = 0;

                        EmprestimoLivro emprestimoLivro = new EmprestimoLivro(idEmprestimo, isDisponivel,dataEmprestimo,dataDevolucao,multaCalculo,idMembroEmprestimo,isbnEmprestimo);
                        emprestimoDAO.registrarEmprestimo(emprestimoLivro);
                    }

                    break;
                case 4: //registrar devolucao
                    break;
                case 5: //consultar emprestimo
                    break;
                case 6: //consultar multa
                    break;
                default:
                System.out.println("Saindo...");
            }

        }while (escolha!=0);


    }
}