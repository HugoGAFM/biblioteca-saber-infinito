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

public class EmprestimoLivro{
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

        try (Connection conn = DataBase.getInstance().getConnection()) {
        
            String verificarPendencia = "SELECT devendo FROM membro WHERE idMembro = ?";
            try (PreparedStatement stmt = conn.prepareStatement(verificarPendencia)) {
                stmt.setInt(1, idMembro);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("Membro não encontrado.");
                    return;
                }
                if (rs.getBoolean("devendo")) {
                    System.out.println("Membro possui pendências. Empréstimo não permitido.");
                    return;
                }
            }
        
            String verificarLivro = "SELECT numCopias FROM livro WHERE ISBN = ?";
            try (PreparedStatement stmt = conn.prepareStatement(verificarLivro)) {
                stmt.setLong(1, isbn);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("Livro não encontrado.");
                    return;
                }
                int copias = rs.getInt("numCopias");
                if (copias <= 0) {
                    System.out.println("📕 Nenhuma cópia disponível deste livro.");
                    return;
                }
            }
        
            this.dataEmprestimo = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataEmprestimo);
            cal.add(Calendar.DAY_OF_MONTH,7);
            this.dataDevolucao = cal.getTime();
            this.isDisponivel = false;

            System.out.println("Empréstimo registrado com sucesso!");
            System.out.println("Data do empréstimo: " + dataEmprestimo);
            System.out.println("Data prevista para devolução: " + dataDevolucao);

            //Gravar no banco
            try(Connection conn = DataBase.getInstance().getConnection()){
                String sql = "INSERT INTO emprestimoLivro (isDisponivel, dataEmprestimo, multaCalculo, idMembro, ISBN) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setBoolean(1, false);
                    stmt.setString(2, new java.sql.Date(dataEmprestimo.getTime()).toString());
                    stmt.setString(3, new java.sql.Date(dataDevolucao.getTime()).toString());
                    stmt.setFloat(4, 0.0f);
                    stmt.setInt(5, this.idMembro);
                    stmt.setLong(6, this.isbn);
                    stmt.executeUpdate();
                    System.out.println("Empréstimo salvo com sucesso no banco de dados!");
                }

                String atualizarEstoque = "UPDATE livro SET numCopias = numCopias - 1 WHERE ISBN = ?";
                try (PreparedStatement stmt = conn.prepareStatement(atualizarEstoque)) {
                    stmt.setLong(1, isbn);
                    stmt.executeUpdate();
                    System.out.println("📦 Estoque atualizado: 1 cópia emprestada.");
                }
                
            } catch (SQLException e) {
                System.err.println("Erro ao salvar empréstimo no banco: " + e.getMessage());
            }
        }
    }

    public void registrarDevolucao(){
        System.out.println("REALIZAR DEVOLUÇÃO");
        System.out.print("Digite o ID do membro: ");
        int idMembro = scanner.nextInt();

        System.out.println("\nLIVROS EMPRESTADOS AO USUÁRIO");

        try (Connection conn = DataBase.getInstance().getConnection()) {
            String consulta = "SELECT idEmprestimo, ISBN, dataEmprestimo FROM emprestimoLivro WHERE idMembro = ? AND isDisponivel = 0";
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                stmt.setInt(1, idMembro);
                ResultSet rs = stmt.executeQuery();

                int contador = 0;
                Map<Integer, Integer> opcoes = new HashMap<>();

                while (rs.next()) {
                    contador++;
                    int idEmp = rs.getInt("idEmprestimo");
                    long isbn = rs.getLong("ISBN");
                    String data = rs.getString("dataEmprestimo");
                    System.out.printf("[%d] Empréstimo #%d - ISBN: %d - Data: %s%n", contador, idEmp, isbn, data);
                    opcoes.put(contador, idEmp);
                }

                if (contador == 0) {
                    System.out.println("Nenhum empréstimo ativo encontrado para este membro.");
                    return;
                }

                System.out.print("Digite o número do empréstimo que deseja devolver: ");
                int escolha = scanner.nextInt();
                Integer idEscolhido = opcoes.get(escolha);

                if (idEscolhido == null) {
                    System.out.println("Escolha inválida.");
                    return;
                }

                // aqui você ainda vai implementar a lógica de cálculo de multa e atualização da devolução
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar empréstimos: " + e.getMessage());
        }
    }
}
