import org.junit.jupiter.api.*;
import org.project.Aplicacao.EmprestimoLivro;
import org.project.Aplicacao.Livro;
import org.project.Aplicacao.Membro;
import org.project.DAO.EmprestimoDAO;
import org.project.DAO.LivroDAO;
import org.project.DAO.MembroDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class DAOTest {
    private static Connection con;
    private static LivroDAO livroDAO;
    private static MembroDAO membroDAO;
    private static EmprestimoDAO emprestimoDAO;

    @BeforeAll
    static void setup() throws SQLException {
        con = DriverManager.getConnection("jdbc:sqlite::memory:");
        Statement stmt = con.createStatement();
        stmt.execute("CREATE TABLE livro (ISBN TEXT PRIMARY KEY, titulo TEXT, autor TEXT, dataPublicacao TEXT, numCopias INTEGER)");
        stmt.execute("CREATE TABLE membro (idMembro INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT, email TEXT, devendo INTEGER DEFAULT 0)");
        stmt.execute("CREATE TABLE emprestimoLivro (idEmprestimo INTEGER PRIMARY KEY AUTOINCREMENT, isDisponivel BOOLEAN, dataEmprestimo TEXT, dataDevolucao TEXT, multaCalculo REAL, idMembro INTEGER, ISBN TEXT)");

        livroDAO = new LivroDAO(con);
        membroDAO = new MembroDAO(con);
        emprestimoDAO = new EmprestimoDAO(con);
    }

    @Test
    @DisplayName("T01 - Cadastrar novo livro")
    void testCadastrarNovoLivro() {
        Livro livro = new Livro("123456789", "Clean Code", "Robert Martin", LocalDate.now(), 3);
        livroDAO.inserirLivro(livro);

        assertTrue(livroDAO.verificarLivroDisponivel(123456789L));
    }

    @Test
    @DisplayName("T02 - Cadastrar livro com ISBN duplicado")
    void testCadastrarLivroISBNRepetido() {
        Livro livro1 = new Livro("987654321", "Livro Único", "Autor", LocalDate.now(), 2);
        livroDAO.inserirLivro(livro1);

        Livro livroDuplicado = new Livro("987654321", "Outro Título", "Outro Autor", LocalDate.now(), 1);
        assertThrows(RuntimeException.class, () -> {
            livroDAO.inserirLivro(livroDuplicado); // Vai falhar por causa da PK duplicada
        });
    }

    @Test
    @DisplayName("T03 - Cadastrar novo membro")
    void testCadastrarNovoMembro() {
        Membro membro = new Membro("Ana", "9999-9999", "ana@email.com");
        membroDAO.cadastrarMembro(membro);

        assertTrue(membro.getIdMembro() > 0);
    }

    @Test
    @DisplayName("T04 - Buscar membro por nome existente")
    void testBuscarMembroPorNome() {
        Membro membro = new Membro("Carlos", "8888-8888", "carlos@email.com");
        membroDAO.cadastrarMembro(membro);

        var lista = membroDAO.buscarTodos();
        assertTrue(lista.stream().anyMatch(m -> m.getNome().equals("Carlos")));
    }

    @Test
    @DisplayName("T05 - Buscar membro por nome inexistente")
    void testBuscarMembroPorNomeInexistente() {
        var lista = membroDAO.buscarTodos();
        boolean encontrado = lista.stream().anyMatch(m -> m.getNome().equals("Zé Ninguém"));
        assertFalse(encontrado, "Nenhum membro chamado 'Zé Ninguém' deve existir.");
    }

    @Test
    @DisplayName("T06 - Realizar empréstimo com cópia disponível")
    void testEmprestimoComCopiaDisponivel() {
        Livro livro = new Livro("111111111", "Java Básico", "Autor X", LocalDate.now(), 2);
        livroDAO.inserirLivro(livro);

        Membro membro = new Membro("João", "1111-1111", "joao@email.com");
        membroDAO.cadastrarMembro(membro);

        EmprestimoLivro emprestimo = new EmprestimoLivro(new Date(), 0.0f, membro.getIdMembro(), 111111111L);
        emprestimoDAO.registrarEmprestimo(emprestimo);

        var emprestimos = emprestimoDAO.consultarEmprestimosAtivosDoMembro(membro.getIdMembro());
        assertFalse(emprestimos.isEmpty());
    }

    @Test
    @DisplayName("T07 - Realizar empréstimo com livro esgotado")
    void testEmprestimoLivroEsgotado() {
        Livro livro = new Livro("222222222", "Livro Esgotado", "Autor Y", LocalDate.now(), 0);
        livroDAO.inserirLivro(livro);

        Membro membro = new Membro("Maria", "2222-2222", "maria@email.com");
        membroDAO.cadastrarMembro(membro);

        boolean disponivel = livroDAO.verificarLivroDisponivel(222222222L);
        assertFalse(disponivel, "Livro não deve estar disponível para empréstimo.");
    }

    @Test
    @DisplayName("T08 - Realizar empréstimo com membro devendo")
    void testEmprestimoComMembroDevendo() {
        Livro livro = new Livro("333333333", "Livro Qualquer", "Autor Z", LocalDate.now(), 2);
        livroDAO.inserirLivro(livro);

        Membro membro = new Membro("Débora", "3333-3333", "debora@email.com");
        membroDAO.cadastrarMembro(membro);

        // Deixa o membro como "devendo" manualmente
        PreparedStatement stmt;
        try {
            stmt = con.prepareStatement("UPDATE membro SET devendo = 1 WHERE idMembro = ?");
            stmt.setInt(1, membro.getIdMembro());
            stmt.executeUpdate();
        } catch (SQLException e) {
            fail("Erro ao simular membro devendo.");
        }

        boolean permitido = membroDAO.verificarDependencias(membro.getIdMembro());
        assertFalse(permitido, "Membro com dívida não deve conseguir emprestar.");
    }

    @Test
    @DisplayName("T09 - Registrar devolução sem atraso")
    void testRegistrarDevolucaoSemAtraso() {
        Livro livro = new Livro("444444444", "Devolução Rápida", "Autor A", LocalDate.now(), 1);
        livroDAO.inserirLivro(livro);

        Membro membro = new Membro("Lucas", "4444-4444", "lucas@email.com");
        membroDAO.cadastrarMembro(membro);

        EmprestimoLivro emprestimo = new EmprestimoLivro(new Date(), 0.0f, membro.getIdMembro(), 444444444L);
        emprestimoDAO.registrarEmprestimo(emprestimo);

        var ids = emprestimoDAO.consultarEmprestimosAtivosDoMembro(membro.getIdMembro());
        assertFalse(ids.isEmpty());

        boolean resultado = emprestimoDAO.registrarDevolucaoController(ids.getFirst());
        assertTrue(resultado, "Devolução deve ser registrada com sucesso.");
    }

    @Test
    @DisplayName("T10 - Registrar devolução com atraso")
    void testRegistrarDevolucaoComAtraso() {
        Livro livro = new Livro("555555555", "Devolução Atrasada", "Autor B", LocalDate.now(), 1);
        livroDAO.inserirLivro(livro);

        Membro membro = new Membro("Helena", "5555-5555", "helena@email.com");
        membroDAO.cadastrarMembro(membro);

        Date dataAntiga = new Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000)); // 10 dias atrás
        EmprestimoLivro emprestimo = new EmprestimoLivro(dataAntiga, 0.0f, membro.getIdMembro(), 555555555L);
        emprestimoDAO.registrarEmprestimo(emprestimo);

        var ids = emprestimoDAO.consultarEmprestimosAtivosDoMembro(membro.getIdMembro());
        assertFalse(ids.isEmpty());

        boolean resultado = emprestimoDAO.registrarDevolucaoController(ids.getFirst());
        assertTrue(resultado);

        String multas = emprestimoDAO.consultarMultaFormatado();
        assertTrue(multas.contains("em atraso"), "Multa deve ser registrada.");
    }

    @Test
    @DisplayName("T11 - Listar livros emprestados por membro")
    void testListarLivrosEmprestadosPorMembro() {
        Livro livro = new Livro("666666666", "Livro Emprestado", "Autor C", LocalDate.now(), 1);
        livroDAO.inserirLivro(livro);

        Membro membro = new Membro("Bruna", "6666-6666", "bruna@email.com");
        membroDAO.cadastrarMembro(membro);

        EmprestimoLivro emprestimo = new EmprestimoLivro(new Date(), 0.0f, membro.getIdMembro(), 666666666L);
        emprestimoDAO.registrarEmprestimo(emprestimo);

        String resultado = emprestimoDAO.consultarEmprestimoFormatado(membro.getIdMembro());
        assertTrue(resultado.contains("666666666"), "ISBN deve estar listado nos empréstimos.");
    }

    @Test
    @DisplayName("T12 - Consultar membros com livros atrasados")
    void testConsultarMembrosComMultas() {
        Livro livro = new Livro("777777777", "Multado", "Autor D", LocalDate.now(), 1);
        livroDAO.inserirLivro(livro);

        Membro membro = new Membro("Igor", "7777-7777", "igor@email.com");
        membroDAO.cadastrarMembro(membro);

        Date dataAntiga = new Date(System.currentTimeMillis() - (15L * 24 * 60 * 60 * 1000)); // 15 dias atrás
        EmprestimoLivro emprestimo = new EmprestimoLivro(dataAntiga, 0.0f, membro.getIdMembro(), 777777777L);
        emprestimoDAO.registrarEmprestimo(emprestimo);

        String resultado = emprestimoDAO.consultarMultaFormatado();
        assertTrue(resultado.contains("em atraso"), "Deve mostrar membro em atraso.");
    }
}


