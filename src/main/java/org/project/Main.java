package org.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.project.Controllers.Navegador;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        TabPane painel = new Navegador().montarPainelPrincipal();
        stage.setScene(new Scene(painel, 800, 600));
        stage.setTitle("Biblioteca Saber Infinito");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
