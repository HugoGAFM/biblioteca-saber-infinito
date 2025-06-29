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

                String buscarEmprestimo = "SELECT dataEmprestimo, ISBN FROM emprestimoLivro WHERE idEmprestimo = ?";
                try (PreparedStatement stmtBusca = conn.prepareStatement(buscarEmprestimo)) {
                    stmtBusca.setInt(1, idEscolhido);
                    ResultSet rs = stmtBusca.executeQuery();

                    if (rs.next()) {
                        String dataEmpStr = rs.getString("dataEmprestimo");
                        long isbnLivro = rs.getLong("ISBN");
                        Date dataEmprestimo = java.sql.Date.valueOf(dataEmpStr);
                
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dataEmprestimo);
                        cal.add(Calendar.DAY_OF_MONTH, 7);
                        Date dataPrevista = cal.getTime();

                        Date hoje = new Date();
                        long diasAtraso = (hoje.getTime() - dataPrevista.getTime()) / (1000 * 60 * 60 * 24);
                        float multa = diasAtraso > 0 ? diasAtraso * 2.0f : 0.0f;

                        String atualizar = "UPDATE emprestimoLivro SET isDisponivel = 1, dataDevolucao = ?, multaCalculo = ? WHERE idEmprestimo = ?";
                        try (PreparedStatement stmtAtualiza = conn.prepareStatement(atualizar)) {
                            stmtAtualiza.setString(1, new java.sql.Date(hoje.getTime()).toString());
                            stmtAtualiza.setFloat(2, multa);
                            stmtAtualiza.setInt(3, idEscolhido);
                            stmtAtualiza.executeUpdate();
                            System.out.println("Devolução registrada!");
                
                            if (multa > 0) {
                                System.out.printf("Multa por atraso: R$ %.2f%n", multa);
                            } else {
                                System.out.println("Livro devolvido no prazo. Sem multa.");
                            }
                        }

                        String devolverCopia = "UPDATE livro SET numCopias = numCopias + 1 WHERE ISBN = ?";
                        try (PreparedStatement stmtEstoque = conn.prepareStatement(devolverCopia)) {
                            stmtEstoque.setLong(1, isbnLivro);
                            stmtEstoque.executeUpdate();
                            System.out.println("Estoque atualizado: a devolução da cópia foi realizada");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar empréstimos: " + e.getMessage());
        }
    }

    public void consultarEmprestimo() {
        System.out.println("CONSULTAR EMPRÉSTIMOS DE UM MEMBRO");
        
        System.out.print("Digite o ID do membro: ");
        int idConsulta = scanner.nextInt();
    
        try (Connection conn = DataBase.getInstance().getConnection()) {
            String sql = "SELECT ISBN, dataEmprestimo FROM emprestimoLivro WHERE idMembro = ? AND isDisponivel = 0";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idConsulta);
                ResultSet rs = stmt.executeQuery();
    
                int contador = 0;
                while (rs.next()) {
                    contador++;
                    long isbn = rs.getLong("ISBN");
                    Date dataEmprestimo = java.sql.Date.valueOf(rs.getString("dataEmprestimo"));
    
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dataEmprestimo);
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                    Date dataPrevista = cal.getTime();
    
                    System.out.println("\nEmpréstimo #" + contador);
                    System.out.println("ISBN: " + isbn);
                    System.out.println("Data do empréstimo: " + dataEmprestimo);
                    System.out.println("Data prevista para devolução: " + dataPrevista);
                }
    
                if (contador == 0) {
                    System.out.println("Este membro não possui livros emprestados no momento.");
                }
    
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar empréstimos: " + e.getMessage());
        }
    }
        public void consultarMultaMembro() {
        System.out.println("MEMBROS COM LIVROS EM ATRASO:\n");
    
        try (Connection conn = DataBase.getInstance().getConnection()) {
            String sql = "SELECT idMembro, dataEmprestimo FROM emprestimoLivro WHERE isDisponivel = 0";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
    
                Map<Integer, Integer> membrosAtrasados = new HashMap<>();
    
                while (rs.next()) {
                    int idMembro = rs.getInt("idMembro");
                    Date dataEmprestimo = java.sql.Date.valueOf(rs.getString("dataEmprestimo"));
    
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dataEmprestimo);
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                    Date dataPrevista = cal.getTime();
    
                    Date hoje = new Date();
                    if (hoje.after(dataPrevista)) {
                        membrosAtrasados.put(idMembro, membrosAtrasados.getOrDefault(idMembro, 0) + 1);
                    }
                }
                
                if (membrosAtrasados.isEmpty()) {
                    System.out.println("Nenhum membro foi encontrado.");
                } else {
                    for (Map.Entry<Integer, Integer> entry : membrosAtrasados.entrySet()) {
                        System.out.printf("Membro #%d - %d livro(s) em atraso%n", entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar membros com multa: " + e.getMessage());
        }
    }
}
