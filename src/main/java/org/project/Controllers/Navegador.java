package org.project.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.Parent;
import org.project.DAO.EmprestimoDAO;
import org.project.DAO.LivroDAO;
import org.project.DAO.MembroDAO;
import org.project.database.BDconnection;

public class Navegador {
    public TabPane montarPainelPrincipal() {
        TabPane tabPane = new TabPane();

        try {
            var con = BDconnection.getConnection();
            var livroDAO = new LivroDAO(con);
            var membroDAO = new MembroDAO(con);
            var emprestimoDAO = new EmprestimoDAO(con);

            FXMLLoader loaderLivro = new FXMLLoader(getClass().getResource("/Paginas/CadastroLivro.fxml"));
            Parent livroPane = loaderLivro.load();
            CadastroLivroController livroCtrl = loaderLivro.getController();
            livroCtrl.setLivroDAO(livroDAO);
            Tab tabLivro = new Tab("Livros", livroPane);

            FXMLLoader loaderMembro = new FXMLLoader(getClass().getResource("/Paginas/CadastroMembro.fxml"));
            Parent membroPane = loaderMembro.load();
            CadastroMembroController membroCtrl = loaderMembro.getController();
            membroCtrl.setMembroDAO(membroDAO);
            Tab tabMembro = new Tab("Membros", membroPane);

            FXMLLoader loaderEmprestimo = new FXMLLoader(getClass().getResource("/Paginas/Emprestimo.fxml"));
            Parent empPane = loaderEmprestimo.load();
            EmprestimoController empCtrl = loaderEmprestimo.getController();
            empCtrl.setDAOs(emprestimoDAO, membroDAO, livroDAO);
            Tab tabEmprestimo = new Tab("Empréstimo", empPane);

            FXMLLoader loaderDev = new FXMLLoader(getClass().getResource("/Paginas/Devolucao.fxml"));
            Parent devolucaoPane = loaderDev.load();
            DevolucaoController devCtrl = loaderDev.getController();
            devCtrl.setEmprestimoDAO(emprestimoDAO);
            Tab tabDevolucao = new Tab("Devolução", devolucaoPane);

            FXMLLoader loaderConsulta = new FXMLLoader(getClass().getResource("/Paginas/Consulta.fxml"));
            Parent ConsultaPane = loaderConsulta.load();
            ConsultaController consultaCtrl = loaderConsulta.getController();
            consultaCtrl.setEmprestimoDAO(emprestimoDAO);
            Tab tabConsulta = new Tab("Consultas", ConsultaPane);

            tabPane.getTabs().addAll(tabLivro, tabMembro, tabEmprestimo, tabDevolucao, tabConsulta);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao montar painel: " + e.getMessage());
        }

        return tabPane;
    }
}