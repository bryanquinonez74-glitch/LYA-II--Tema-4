package com.brayansystem.analizadorsemantico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AnalizadorSApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AnalizadorSApplication.class.getResource("MenuP.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 750);
        stage.setTitle("Analizador Semántico ");
        stage.setScene(scene);
        stage.show();
    }
}
