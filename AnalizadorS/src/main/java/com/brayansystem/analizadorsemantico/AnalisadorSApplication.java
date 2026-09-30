package com.brayansystem.analizadorsemantico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AnalisadorSApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AnalisadorSApplication.class.getResource("MenuP.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle("Analizador Semántico - Lenguajes y Autómatas II");
        stage.setScene(scene);
        stage.show();
    }
}
